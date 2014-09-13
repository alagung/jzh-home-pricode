package com.sb.skin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import android.util.Log;

public class SystemUtil {
	private ArrayList<String> commands = new ArrayList<String>();
	private StringBuffer output;

	public static String readFile(String fileName) {
		try {
			File file = new File(fileName);

			FileInputStream fis = new FileInputStream(file);

			int length = fis.available();

			byte[] buffer = new byte[length];
			fis.read(buffer);

			String res = EncodingUtils.getString(buffer, "UTF-8");

			fis.close();
			return res;
		} catch (Exception e) {
			return "";
		}
	}

	public static void writeFile(String fileName, String write_str) {
		try {

			File file = new File(fileName);

			FileOutputStream fos = new FileOutputStream(file);

			byte[] bytes = write_str.getBytes();

			fos.write(bytes);

			fos.close();
		} catch (Exception e) {

		}
	}

	public static boolean canRunRootCommands() {
		boolean retval = false;
		Process suProcess;

		try {
			suProcess = Runtime.getRuntime().exec("su");

			DataOutputStream os = new DataOutputStream(
					suProcess.getOutputStream());
			DataInputStream osRes = new DataInputStream(
					suProcess.getInputStream());

			if (null != os && null != osRes) {
				// Getting the id of the current user to check if this is root
				os.writeBytes("id\n");
				os.flush();

				String currUid = osRes.readLine();
				boolean exitSu = false;
				if (null == currUid) {
					retval = false;
					exitSu = false;
					Log.d("ROOT", "Can't get root access or denied by user");
				} else if (true == currUid.contains("uid=0")) {
					retval = true;
					exitSu = true;
					Log.d("ROOT", "Root access granted");
				} else {
					retval = false;
					exitSu = true;
					Log.d("ROOT", "Root access rejected: " + currUid);
				}

				if (exitSu) {
					os.writeBytes("exit\n");
					os.flush();
				}
			}
		} catch (Exception e) {
			// Can't get root !
			// Probably broken pipe exception on trying to write to output
			// stream after su failed, meaning that the device is not rooted

			retval = false;
			Log.d("ROOT", "Root access rejected [" + e.getClass().getName()
					+ "] : " + e.getMessage());
		}

		return retval;
	}

	public final boolean execute() {
		boolean retval = false;

		try {
			if (null != commands && commands.size() > 0) {
				Process process = Runtime.getRuntime().exec("su");

				DataOutputStream os = new DataOutputStream(
						process.getOutputStream());

				for (String currCommand : commands) {
					Log.i("ROOT", "Running: " + currCommand);
					os.writeBytes(currCommand + "\n");
					os.flush();
				}

				os.writeBytes("exit\n");
				os.flush();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				int read;
				char[] buffer = new char[4096];
				output = new StringBuffer();
				while ((read = reader.read(buffer)) > 0) {
					output.append(buffer, 0, read);
				}
				reader.close();

				try {
					int suProcessRetval = process.waitFor();
					if (255 != suProcessRetval) {
						retval = true;
						Log.i("ROOT", "Success Return");
					} else {
						retval = false;
						Log.i("ROOT", "Error Return: " + retval);
					}
				} catch (Exception ex) {
					// Log.e("Error executing root action", ex);
				}
			}
		} catch (IOException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (SecurityException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (Exception ex) {
			Log.w("ROOT", "Error executing internal operation", ex);
		}

		return retval;
	}

	public final boolean executeNormal() {
		boolean retval = false;

		try {
			if (null != commands && commands.size() > 0) {
				output = new StringBuffer();
				for (String currCommand : commands) {
					Log.i("NONROOT", "Running: " + currCommand);
					Process process = Runtime.getRuntime().exec(currCommand);
					
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(process.getInputStream()));
					int read;
					char[] buffer = new char[4096];
					while ((read = reader.read(buffer)) > 0) {
						output.append(buffer, 0, read);
					}
					reader.close();
					output.append("\n");

					try {
						int suProcessRetval = process.waitFor();
						if (255 != suProcessRetval) {
							retval = true;
							Log.i("NONROOT",
									"Success Return");
						} else {
							retval = false;
							Log.i("NONROOT",
									"Error Return: " + retval);
						}
					} catch (Exception ex) {
						// Log.e("Error executing root action", ex);
					}
				}
			}
		} catch (IOException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (SecurityException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (Exception ex) {
			Log.w("ROOT", "Error executing internal operation", ex);
		}

		return retval;
	}

	ArrayList<String> getCommands() {
		return commands;
	}

	void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}

	void setCommand(String command) {
		this.resetCommand();
		this.addCommand(command);
	}

	void resetCommand() {
		this.commands.clear();
	}

	void addCommand(String command) {
		this.commands.add(command);
	}

	String getOutput() {
		return output.toString();
	}
}
