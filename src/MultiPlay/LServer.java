package MultiPlay;
import java.io.*;
import java.net.*;
import java.util.*;

public class LServer {
	boolean started = false;
	ServerSocket ss = null;
	
	List<Client> clients = new ArrayList<Client>();
	
	int id=2;
	int com=0;
	
	public static void main(String[] args) {
		new LServer().start();
	}
	
	public void start() {
		try {
			ss = new ServerSocket(5555);
			started = true;
		} catch (BindException e) {
			System.out.println("端口使用中....");
			System.out.println("请关掉相关程序并重新运行服务器！");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			while(started) {
				
				if(clients.size()<=4)
				{
				Socket s = ss.accept();
				
				Client c = new Client(s);
				
				c.dos.writeInt(id);
				id++;
				if(id==6)
				{
					id=2;
				}
				
				String read="";
				read=c.dis.readUTF();
//				System.out.println(read);
				
				String[] msg=read.split(" ");
				
				c.setId(Integer.parseInt(msg[0]));
				c.setName(msg[1]);
				c.setWin(msg[2]);
				c.setLevel(msg[3]);
				
				clients.add(c);
				
				
				for(int i=0;i<clients.size();i++)
				{

					Client c0=clients.get(i);
					String str=c0.getId()+" "+c0.getName()+" "+c0.getWin()+" "+c0.getLevel();
					
					for(int j=0;j<clients.size();j++)
					{
						Client c1=clients.get(j);
						c1.dos.writeUTF(str);
					}
				}
					
                System.out.println("a client connected!");
				new Thread(c).start();
				}
				else
				{
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
		
				e.printStackTrace();
			}
		}
	}
	
	class Client implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		
		String name;
		String win;
		String level;
		int id;
		
		public Client(Socket s) {
			
			
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//----------------------------------------------
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getWin() {
			return win;
		}

		public void setWin(String win) {
			this.win = win;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}
		//------------------------------------------------------------

		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("对方退出了！我从List里面去掉了！");
		
			}
		}
		
		public void run() {
			try {
				while(bConnected) {
					
					String str = dis.readUTF();
//                    System.out.println(str);///////////////////////////////
					for(int i=0; i<clients.size(); i++) {
						Client c = clients.get(i);
						c.send(str);

					}
					
				}
			} catch (EOFException e) {
				System.out.println("Client closed!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null)  {
						s.close();
	
					}
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
			}
		}
		
	}
}
