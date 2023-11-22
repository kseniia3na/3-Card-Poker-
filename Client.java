import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{

	
	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;
	private Consumer<Serializable> callback;
	String IPAddress;
	int PortNUM;

	Client(Consumer<Serializable> call, String ip_address, String port_num){
			callback = call;
			this.IPAddress = ip_address;
			this.PortNUM = Integer.parseInt(port_num);
	}
	
	public void run() {
		
		try {
			socketClient= new Socket(IPAddress,PortNUM);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			System.out.println("Error on run client");
			e.printStackTrace();
		}
		
		while(true) {
			try {
				PokerInfo message = (PokerInfo) in.readObject();
				System.out.println("received data");
				callback.accept(message);
			}
			catch(Exception e) {
				System.out.println("Error on received data, trying string");
//				e.printStackTrace();

				try {
					String message = (String) in.readObject();
					callback.accept(message);
				} catch (Exception ex) {
					System.out.println("Well this didnt work");
				}

			}
		}
	
    }

	
	public void send(PokerInfo data) {
		try {
			out.reset();
			out.writeObject(data);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
