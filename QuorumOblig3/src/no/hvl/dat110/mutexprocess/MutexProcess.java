package no.hvl.dat110.mutexprocess;

import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.Operation;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.hvl.dat110.interfaces.ProcessInterface;
import no.hvl.dat110.util.Util;

public class MutexProcess extends UnicastRemoteObject implements ProcessInterface {

	private static final long serialVersionUID = 1L;
	
	private int processId;
	private String procStubname;
	private int counter;

	private List<Message> queueACK; 						// queue for acknowledged messages
	
	private File localfile;									// a shared resource; each replica has own local copy
	private String filename = "file1.txt";
	private int version = 0;
	private List<String> replicas;							// list of other processes including self known to this process
	private boolean CS_BUSY = false;						// Lock: indicate that critical section is being accessed (e.g. accessing a shared resource) 
	private boolean WANTS_TO_ENTER_CS = false;				// Lock: indicate process wants to enter CS
	private int N; 											// number of replicas storing a replicated resource - Not all processes
	private int quorum;										// quorum needed to be granted access

	
	protected MutexProcess(int procId, String stubName) throws RemoteException {
		super();
		this.processId = procId;
		this.procStubname = stubName;
		counter = 0;
	
		queueACK = new ArrayList<Message>();
		queueACK = Collections.synchronizedList(queueACK); 				// make sure to synchronize this list
		
		replicas = Util.getProcessReplicas();
		N = replicas.size();
		quorum = N/2 + 1;
		
		createFile();										// create a local file - this is our shared resource (item)
	}
	
	private void createFile() {
		String path = new File(".").getAbsolutePath().replace(".", "");
		System.out.println(path);
		path = path+"/"+procStubname+"/";
		File fpath = new File(path);
		if(!fpath.exists()) {
			boolean suc = fpath.mkdir();
			try {
				if(suc) {
					File file = new File(fpath+"/"+filename);
					file.createNewFile();
					
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		this.setFilename(fpath.getAbsolutePath()+"/"+filename);
	}
	
	public void incrementclock() throws RemoteException {
		counter++;
	}
	
	public void acquireLock() throws RemoteException {
		// logical clock update and set CS variable
		incrementclock();
		CS_BUSY=true;
	}
	
	public void releaseLocks() throws RemoteException {
		// release your lock variables and logical clock update
		CS_BUSY=false;
		WANTS_TO_ENTER_CS=false;
		incrementclock();
	}
	
	public boolean requestWriteOperation(Message message) throws RemoteException {
		incrementclock();								// increment clock 
		message.setClock(counter);						// set the timestamp of message
		message.setProcessID(processId);				// set the process ID
		message.setOptype(OperationType.WRITE);

		WANTS_TO_ENTER_CS = true;
		
		// multicast read request to start the voting to N/2 + 1 replicas (majority) - optimal. You could as well send to all the replicas that have the file
		
		
		return multicastMessage(message,quorum);		// change to the election result
	}
	
	public boolean requestReadOperation(Message message) throws RemoteException {
		incrementclock();								// increment clock 
		message.setClock(counter);						// set the timestamp of message
		message.setProcessID(processId);				// set the process ID
		message.setOptype(OperationType.READ);
		

		WANTS_TO_ENTER_CS = true;
		
		// multicast read request to start the voting to N/2 + 1 replicas (majority) - optimal. You could as well send to all the replicas that have the file
		
		
		return multicastMessage(message,quorum);  // change to the election result
	}
	
	// multicast message to N/2 + 1 processes (random processes)
	private boolean multicastMessage(Message message, int n) throws AccessException, RemoteException {
		
		replicas.remove(this.procStubname);			// remove this process from the list
		
		 
		// randomize - shuffle list each time - to get random processes each time
		
		Collections.shuffle(replicas);
		
		// multicast message to N/2 + 1 processes (random processes) - block until feedback is received
		synchronized(queueACK){
		queueACK.clear();
			for(int i=0; i<replicas.size(); i++) {
		
			String stub = replicas.get(i);
			
			ProcessInterface p;
			try {
				p = Util.registryHandle(stub);
				
//					Operations op = new Operations(p, message);
//					op.performOperation(); 		
					queueACK.add(p.onMessageReceived(message));
				
				
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
		return majorityAcknowledged(); 
		}
		// do something with the acknowledgement you received from the voters - Idea: use the queueACK to collect GRANT/DENY messages and make sure queueACK is synchronized!!!
		
		// compute election result - Idea call majorityAcknowledged()
		
		 // change to the election result			

	}
	
	@Override
	public Message onMessageReceived(Message message) throws RemoteException {
		
		// increment the local clock
		incrementclock();
		// Hint: for all 3 cases, use Message to send GRANT or DENY. e.g. message.setAcknowledgement(true) = GRANT
		
		Message reply= new Message();
		reply.setProcessStubName(procStubname);
		reply.setClock(counter);
		/**
		 *  case 1: Receiver is not accessing shared resource and does not want to: GRANT, acquirelock and reply
		 */
		ProcessInterface p;
		if(!WANTS_TO_ENTER_CS&&!CS_BUSY) {
			reply.setAcknowledged(true);
			acquireLock();
			return reply;
		/**
		 *  case 2: Receiver already has access to the resource: DENY and reply
		 */
		}else if(CS_BUSY) {
			reply.setAcknowledged(false);
			return reply;	
		
		
		/**
		 *  case 3: Receiver wants to access resource but is yet to (compare own multicast message to received message
		 *  the message with lower timestamp wins) - GRANT if received is lower, acquirelock and reply
		 */
		}else if(WANTS_TO_ENTER_CS) {
			if(counter>message.getClock()) {
				reply.setAcknowledged(true);
				acquireLock();
				return reply;
			}
			
		}
	return null;
	}	
	
	public boolean majorityAcknowledged() throws RemoteException {
		
		// count the number of yes (i.e. where message.isAcknowledged = true)
		// check if it is the majority or not
		// return the decision (true or false)

		int y = 0;
		int n = 0;
		
		for(Message m : queueACK) {
			if(m.isAcknowledged()) {
				y++;
			}else {
				n++;
			}
		}
				
		return y>n;			// change this to the result of the vote
	}

		
	@Override
	public void onReceivedVotersDecision(Message message) throws RemoteException {
		
		// release CS lock if voter initiator says he was denied access bcos he lacks majority votes
		if(!message.isAcknowledged())
		releaseLocks();
		// otherwise lock is kept

	}

	@Override
	public void onReceivedUpdateOperation(Message message) throws RemoteException {
		
		// check the operation type: we expect a WRITE operation to do this. 
		if(message.getOptype()==OperationType.WRITE) {
			// perform operation by using the Operations class 
			Operations op = new Operations(this,message);
			op.performOperation();
			releaseLocks();
			// Release locks after this operation
		}
		
		
	}
	
	@Override
	public void multicastUpdateOrReadReleaseLockOperation(Message message) throws RemoteException {
		
		// check the operation type:
		Operations op = new Operations(this,message);
		if(message.getOptype()==OperationType.WRITE) {
			// if this is a write operation, multicast the update to the rest of the replicas (voters)
			op.multicastOperationToReplicas(message);	
		}else if(message.getOptype()==OperationType.READ){
			// otherwise if this is a READ operation multicast releaselocks to the replicas (voters)
			op.multicastReadReleaseLocks();
		}
	}	
	
	@Override
	public void multicastVotersDecision(Message message) throws RemoteException {	
		// multicast voters decision to the rest of the replicas
		for(int i = 0; i<replicas.size();i++) {
			String stub = replicas.get(i);
		try {
			ProcessInterface p = Util.registryHandle(stub);
			p.onReceivedVotersDecision(message);
		}catch(NotBoundException e) {
			e.printStackTrace();
		}
		}
	}

	@Override
	public int getProcessID() throws RemoteException {
		
		return processId;
	}

	@Override
	public int getVersion() throws RemoteException {
		
		return version;
	}

	public File getLocalfile() {
		return localfile;
	}

	public void setLocalfile(File localfile) {
		this.localfile = localfile;
	}

	@Override
	public void setVersion(int version) throws RemoteException {
		
		this.version = version;
	}

	public String getFilename() throws RemoteException {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}	

}
