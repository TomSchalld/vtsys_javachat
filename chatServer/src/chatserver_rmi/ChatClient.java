/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatserver_rmi;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author Thomas Schalldach & Fabian Brammer Copyright 2015
 */
public class ChatClient implements Serializable {
	ChatProxy cp;
	ClientProxy client;
	String username;
	ChatServer server;
	
	
	public ChatClient() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		this.username = IO.readString("Username: ");
		String serverAddress = IO.readString("IP-Adresse des Servers: ");
		this.server= (ChatServer) Naming.lookup ("rmi://"+serverAddress+":1099/ChatServer");
		this.client = new ClientProxyImpl(this);
	    System.out.println(this.username+" wird eingeloggt ...");
		this.cp = server.subscribeUser(this.username, client);
		if(cp!=null){
    		System.out.println("... done.");
		}
	}
	public void logout () throws RemoteException{
		if(this.server.unsubscribeUser(this.username)){
			System.out.println(this.username+" erfolgreich ausgeloggt.");
		}else{
			System.out.println(this.username+" fehler beim ausloggen.");
		}
	}
	public void recieveMessage(String username, String message)
			throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(username+" schreibt:  "+message);
	}
	public void sendMessage(String message) throws RemoteException {
	    this.cp.sendMessage(message);
	  }
	
	public ChatProxy getCp() {
		return cp;
	}
	public ClientProxy getClient() {
		return client;
	}
	public String getUsername() {
		return username;
	}
	public ChatServer getServer() {
		return server;
	}
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
        try {
        	ChatClient client = new ChatClient();  	    
    		
    		
    		while(true){
        	    String massage = IO.readString("@"+client.getUsername()+": ");
        	    if(massage.equals("exit")){
        	    	client.logout();
        	    	break;
        	    }
    			client.sendMessage(massage);
    		}
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
	}

}
