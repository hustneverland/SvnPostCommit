package mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellInvoker {
	
	public String exec(String cmd) throws IOException, InterruptedException {
		Process pc = Runtime.getRuntime().exec(cmd);
		pc.waitFor();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(pc.getInputStream()));
		
		StringBuffer sb = new StringBuffer();
		String line = "";
		while((line = reader.readLine()) != null) {
			if(sb.length() == 0) {
				sb.append(line);
			} else {
				sb.append("\n" + line);
			}
		}
		return sb.toString();
	}
	
	public String execOneLine(String cmd) throws IOException, InterruptedException {
		Process pc = Runtime.getRuntime().exec(cmd);
		pc.waitFor();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(pc.getInputStream()));
		return reader.readLine();
	}
}
