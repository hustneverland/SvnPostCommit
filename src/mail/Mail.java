package mail;

public class Mail {
	
	public static void main(String[] args) {
		Mail sendMail = new Mail();
		String[] mail = sendMail.constructMail();
		// FIXME use linux sendmail is better
		SendEmail.sendMail(mail[0], mail[1], null);
	}
	
	private String[] constructMail() {
		ShellInvoker inv = new ShellInvoker();
		try {
			/*inv.execOneLine("export LANG=zh_CN.UTF-8");*/
			String project = inv.execOneLine("/usr/bin/svnlook dirs-changed " + PropUtil.getString("svn_project_path"));
			project = project.split("/")[0]; // 去除斜杠
			String changed = inv.exec("/usr/bin/svnlook changed " + PropUtil.getString("svn_project_path"));
			String author = inv.execOneLine("/usr/bin/svnlook author " + PropUtil.getString("svn_project_path"));
			String date = inv.execOneLine("/usr/bin/svnlook date " + PropUtil.getString("svn_project_path"));
			String log = inv.exec("/usr/bin/svnlook log " + PropUtil.getString("svn_project_path"));
			String youngest = inv.execOneLine("/usr/bin/svnlook youngest " + PropUtil.getString("svn_project_path"));
			
			String mailTitle = "[" + project + "] svn changed by [" + author + "] at " + date;
			StringBuffer mailContent = new StringBuffer();
			mailContent.append("Files Changed :\n");
			mailContent.append(changed + "\n\n");
			mailContent.append("Changed Log :\n");
			mailContent.append(log+"\n\n");
			mailContent.append("Newest version : " + youngest);
			
			return new String[]{mailTitle, mailContent.toString()};
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
