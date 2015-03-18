package com.ip.kamera;

import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.probat.R;




class MotionDetection extends AsyncTask<String, Void, Bitmap[]> { 
	 Toast toast;
	 private Context mContext;
	 private TextView mText;
	 public MotionDetection(Context context, TextView text) {
		 mContext = context; 
	     mText = text;
	  }

	  protected Bitmap[] doInBackground(String... urls) {
		  if (isCancelled())  
			  return(null);

	      String urldisplay = urls[0];
	      Bitmap mIcon11 = null;
	      Bitmap mIcon12 = null;
	      // Dio koda kojim se dohvaća okvir kamere
	      try {
	        InputStream in = new java.net.URL(urldisplay).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	      } catch (Exception e) {
	          Log.e("Error", e.getMessage());
	          e.printStackTrace();
	      }
	      // Čekanje pola sekunde...
	      try {
	            Thread.sleep(500);
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	        }
	      // Dohvaćanje drugog okvira pola sekunde poslije prvog
	      try {
		        InputStream inn = new java.net.URL(urldisplay).openStream();
		        mIcon12 = BitmapFactory.decodeStream(inn);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		      }
	      //Reuzltat se šalje u obliku polja od dvije slike
	      Bitmap[] arrayOfBitmap = {mIcon11, mIcon12};
	      return arrayOfBitmap;
	      }

	  protected void onPostExecute(Bitmap result[]) {
		  int w = 320;
		  int h = 240;
		  //Pretvaranje slika u polje piksela
		  int[] array1 = new int[w*h];  
		  int[] array2 = new int[w*h]; 
		  result[0].getPixels(array1, 0, w, 0, 0, w, h);  
		  result[1].getPixels(array2, 0, w, 0, 0, w, h); 
		  
		  // Ako ima pokreta ispiši poruku i pokreni alarm, ako nema ispiši poruku
		  if (isDifferent (array1, array2, w,  h)) {		  
		    	mText.setText("STATUS: Pokret detektiran!");
		    	playAlertTone();    
		  	}
		  else mText.setText("STATUS: Nema pokreta.");
		 
		  // Ponovno pokretanje dohvaćanja video okvira
		  new MotionDetection(mContext, mText).execute("http://kamerica.safe100.net:10000/cgi-bin/viewer/video.jpg");


		  }
	  // Funkcija koja prilikom pokretanja izvršava zvučni alarm
	  public  void playAlertTone() {
		  Thread t = new Thread() {
	            public void run() {
	                MediaPlayer player = null;
	                player = MediaPlayer.create(mContext,R.raw.beep);
	                player.start();
	                }
		  };
		  t.start();
	  }
	  
	  // Algoritam za detekciju pokreta
	   public boolean isDifferent(int[] first, int[] second, int width, int height) {
		    int mPixelThreshold = 25; // Vrijednosti su određene ekperimentiranjem.
		    int mThreshold = 7500;  
		    int totDifferentPixels = 0; // Pomoćna varijabla koja broji različite piksele
		    
		    // Petlja kojom se prolazi kroz sve piksele oba okvira.
		      for (int i = 0, ij = 0; i < height; i++) {
		          for (int j = 0; j < width; j++, ij++) {
		        	  // „Maskiranje“ pomoću AND kojim dobijemo samo plavu vrijednost okvira u jednom 
		        	  // integeru, tako da se uzima po jedan bit iz cijelog polja.
		              int pix = (0xff & ((int) first[ij]));
		              int otherPix = (0xff & ((int) second[ij]));
		              
		              // Uhvati svaki piksel koji je izvan dosega
		              if (pix < 0) pix = 0;
		              if (pix > 255) pix = 255;
		              if (otherPix < 0) otherPix = 0;
		              if (otherPix > 255) otherPix = 255;
		              // Na kraju se oduzimaju polja prvog i drugog okvira.
		              // Ako je razlika veća od definirane granice, povečaj broj različitih piksela.
		              if (Math.abs(pix - otherPix) >= mPixelThreshold) {
		                  totDifferentPixels++;
		                  // Obojaj drugačiji piksel crvenom
		                  // first[ij] = Color.RED; // Pomočna linija za manipulaciju okvira
		              }
		          }
		      }
		      if (totDifferentPixels <= 0) totDifferentPixels = 1;
		       // Ako je ukupan broj različitih piksela veći od druge granice, pokret je detektiran
		      boolean different = totDifferentPixels > mThreshold;
		      // Dio koji mjeri postotak različitosti okvira i ispusuje ga na zaslon
		      int size = height * width; 
		      int percent = 100/(size/totDifferentPixels); 
		      String output = "Razlika: "   + percent + "%"; 
		      Toast.makeText(mContext, output, Toast.LENGTH_SHORT).show();
		       
		      return different;
		  }
		  
	}