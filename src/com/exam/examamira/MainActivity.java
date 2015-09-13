package com.exam.examamira;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button create, capture, send;
	private static final int CAMERA_PIC_REQUEST = 1111;
	private ImageView mImage;
	String email, subject, message, attachmentFile;
	Uri URI = null;
	int columnIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initObject();
	}

	private void initView() {
		create = (Button) findViewById(R.id.btnCreate);
		capture = (Button) findViewById(R.id.btnCapture);
		send = (Button) findViewById(R.id.btnSend);
		mImage = (ImageView) findViewById(R.id.imageView1);
	}

	private void initObject() {
		// to create text file
		create.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String FileName = "mydata.txt";
				String Body = "Amira Ali \n 26-6-1992 \n computer and system \n engmero1992@gmail.com";
				File root = new File(Environment.getExternalStorageDirectory(),
						"mydatafolder");

				if (!root.exists()) {
					Toast.makeText(getApplicationContext(),
							"error create file!", Toast.LENGTH_SHORT).show();
					root.mkdirs();
				}
				File file = new File(root, FileName);
				try {
					FileWriter writer = new FileWriter(file);
					writer.append(Body);
					writer.flush();
					writer.close();
					Toast.makeText(getApplicationContext(), "saved",
							Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "error!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// to take photo from camera
		capture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_PIC_REQUEST);
			}
		});

		// to send an email
		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String to = "omarezz.developer@gmail.com";
				String cc = "engmero1992@gmail.com";
				
//				String aa = Environment.getExternalStorageDirectory()
//						+ "mydatafolder" + "mydata.txt";
//				String bb = Environment.getExternalStorageDirectory()
//						+ "mydatafolder" + "image.jpg";
//				Toast.makeText(getApplicationContext(), aa, Toast.LENGTH_LONG)
//						.show();
//				// or
//				String[] filePaths = new String[] { aa, bb };
				
				String[] filePaths = new String[] {
						"storage/sdcard0/mydatafolder/mydata.txt",
						"storage/sdcard0/mydatafolder/image.jpg" };
				email(to, cc, "final project", "Amira Ali Abd Elazeam ",
						filePaths);
			}
		});

	}

	// for send mail
	public void email(String to, String cc, String subject, String emailText,
			String[] filePaths) {

		final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { to });
		emailIntent.putExtra(android.content.Intent.EXTRA_CC,
				new String[] { cc });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
		ArrayList<Uri> uris = new ArrayList<Uri>();
		for (String file : filePaths) {
			File fileIn = new File(file);
			Uri u = Uri.fromFile(fileIn);
			uris.add(u);
		}
		emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	// for camera
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			mImage.setImageBitmap(thumbnail);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			File root = new File(Environment.getExternalStorageDirectory(),
					"mydatafolder");
			File file = new File(root, "image.jpg");
			try {
				file.createNewFile();
				FileOutputStream fo = new FileOutputStream(file);
				fo.write(bytes.toByteArray());
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
