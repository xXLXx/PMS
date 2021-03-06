package com.qrc.pms.helper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.qrc.pms.config.Config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;



public class FileHelper {

	
	 	private static int serverResponseCode = 0;	    
	    private static String upLoadServerUri = null;
	   
	
		public static void savedFile(File pictureFile, byte image[])
		{	
			try
			{
				  FileOutputStream fos = new FileOutputStream(pictureFile);
				  fos.write(image);
				  fos.close();
				  fos.flush();
		        
			}
			catch (FileNotFoundException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		
		public static void sendFiles(List<String> subRoot, Activity act) 
		{
			Log.e("subroot", ""+subRoot);
			
			for(String subsubRoot : subRoot)
			{
				File f = new File(subsubRoot);
				Log.e("EEEE", ""+f);
				uploadFile(""+ subsubRoot, act, 0);
//				File[] files = f.listFiles(); 
//	
//				for(int x = 0; x < files.length; x++)
//				{	
//					Log.e("dsfdfgdfg", "akfjsjg");
//					
//					int i = -1;	
//					
//					i = uploadFile(""+ files[x], act, x);
//					
//					
//					while(i == -1) 
//					{
//						try 
//						{							
//							Thread.sleep(100);
//						} 
//						catch (InterruptedException e) 
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
				
			
			
			}
		
		}
		
		public static List<String> getFiles(String parent){
			List<String> list = new ArrayList<String>();
			File files = new File(parent);
			File[] allfiles =  files.listFiles();
			
			for(File file : allfiles)
			{
				if(file.isDirectory())
				{
					list.add("" + file);
				}
			}
			
			return list;			
		}
		
		
		
		public static List<String> getLastFile(String parent){
			
			File files = new File(parent);
			File[] allfiles =  files.listFiles();
			List<String> filesList = new ArrayList<String>();
			filesList.add(allfiles[allfiles.length - 1].toString());
			return filesList;
				
		}
		
		
		
		
		// para ni sa na save na images na walay net. .	
		
		
		 public static int uploadFile(String sourceFileUri,  Activity a, int index) {
			 
			 Log.e("SDFDFdf", "" + sourceFileUri);
			 upLoadServerUri = Config.BASE_URL + "upload_file.php";
			 // upLoadServerUri = "ip_address/upload_file.php";
			 Log.e("SDFDFdf", ""+upLoadServerUri); 
			 
	    	  String fileName = sourceFileUri;
	 
	          HttpURLConnection conn = null;
	          DataOutputStream dos = null;  
	          String lineEnd = "\r\n";
	          String twoHyphens = "--";
	          String boundary = "*****";
	          int bytesRead, bytesAvailable, bufferSize;
	          byte[] buffer;
	          int maxBufferSize = 1 * 1024 * 1024; 
	          File sourceFile = new File(sourceFileUri); 
	          
	          if (!sourceFile.isFile()) {
		           
		           a.runOnUiThread(new Runnable() {
		               public void run() {

		               }
		           }); 
		           
		           return 0;
	           
	          }
	          else
	          {
		           try {
		            	 // open a URL connection to the Servlet
		               FileInputStream fileInputStream = new FileInputStream(sourceFile);
		               URL url = new URL(upLoadServerUri);
		               Log.e("FSAFASF", "" +url);
		               // Open a HTTP  connection to  the URL
		               conn = (HttpURLConnection) url.openConnection(); 
		 	           conn.setConnectTimeout(100000);
		               conn.setDoInput(true); // Allow Inputs
		               conn.setDoOutput(true); // Allow Outputs
		               conn.setUseCaches(false); // Don't use a Cached Copy
		               conn.setRequestMethod("POST");
		               
		               conn.setRequestProperty("Connection", "Keep-Alive");
		               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		               
		               fileName = fileName.replace(".csis", "");
		               
		               conn.setRequestProperty("uploaded_file", fileName); 
		               Log.e("FILE", ""+ fileName);
		               dos = new DataOutputStream(conn.getOutputStream());
		               Log.e("SADASD",""+ conn);
		               dos.writeBytes(twoHyphens + boundary + lineEnd); 
		               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
		            		                     + fileName + "\"" + lineEnd);
		            
		               dos.writeBytes(lineEnd);
		     
		               // create a buffer of  maximum size
		               bytesAvailable = fileInputStream.available(); 
		     
		               bufferSize = Math.min(bytesAvailable, maxBufferSize);
		               buffer = new byte[bufferSize];
		     
		               // read file and write it into form...
		               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
		                 
		               while (bytesRead > 0) {
		            	   
		                 dos.write(buffer, 0, bufferSize);
		                 bytesAvailable = fileInputStream.available();
		                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
		                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
		                 
		                }
		     
		               // send multipart form data necesssary after file data...
		               dos.writeBytes(lineEnd);
		               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		     
		               // Responses from the server (code and message)
		               serverResponseCode = conn.getResponseCode();
		               String serverResponseMessage = conn.getResponseMessage();
		                
		               Log.i("uploadFile", "HTTP Response is : " 
		            		   + serverResponseMessage + ": " + serverResponseCode);
		               
		               if(serverResponseCode == 200){
		            	
		            	
		            	  
		                   a.runOnUiThread(new Runnable() {
		                        public void run() {

		                      
		                        }
		                    });                
		               }    
		               
		               //close the streams //
		               fileInputStream.close();
		               dos.flush();
		               dos.close();
		                
		          } catch (MalformedURLException ex) {
		        	  
		             // dialog.dismiss();  
		              ex.printStackTrace();

		              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
		          } catch (Exception e) {
		        	  
		              //dialog.dismiss();  
		              e.printStackTrace();
		              
		          }    
		          return serverResponseCode; 
		          
	           } // End else block 
	         } 
		 
		 
		 public static void deleteChildFolder(File childDirectory)
		 {
			 
			 if(childDirectory.isDirectory())
			 {
				for (File child : childDirectory.listFiles())
		        {
		            child.delete();
		        }
			 }
			 childDirectory.delete();
			 
		 }
		
		 
		

}
