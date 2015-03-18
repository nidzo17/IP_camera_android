package com.ip.kamera;




import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.probat.R;


public class MainActivity extends Activity {
	String str = "rtsp://kamerica.safe100.net/live2.sdp"; // URL IP kamere
	TextView text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	text = (TextView) findViewById(R.id.textv);
        super.onCreate(savedInstanceState);
        setActivityBackgroundColor(); //Postavljanje pozadine
        setContentView(R.layout.activity_main); 

        //Inicijaliziranje video pogleda i Media kontrolera
        VideoView videoView = (VideoView)findViewById(R.id.video_view);  
        MediaController mc = new MediaController(this); 
        videoView.setMediaController(mc);
        
        Uri uri = Uri.parse(str); // str je URL adresa kamere
        videoView.setVideoURI(uri);
        //Pozivanje funkcija koje se obavljaju prije i poslije
        //prikaza videa i funkcije koja se poziva ako je uhvaćena greška
        videoView.setOnCompletionListener(videoViewCompletionListener);
        videoView.setOnPreparedListener(videoViewPreparedListener);
        videoView.setOnErrorListener(videoViewErrorListener);

        videoView.requestFocus();
        videoView.start(); //pokretanje video toka    
    }
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    MediaPlayer.OnCompletionListener videoViewCompletionListener
    = new MediaPlayer.OnCompletionListener(){

    	// Ako je je završen video odnosno ako se izgubila veza s kamerom
    	// ponovno pozivi video tok
    public void onCompletion(MediaPlayer mp) {
    	VideoView videoView = (VideoView)findViewById(R.id.video_view);
        MediaController mc = new MediaController(MainActivity.this);
        videoView.setMediaController(mc); 

        Uri uri = Uri.parse(str);
        videoView.setVideoURI(uri);
       
        videoView.requestFocus();
        videoView.start();  
        }
       };
      
     MediaPlayer.OnPreparedListener videoViewPreparedListener
     = new MediaPlayer.OnPreparedListener(){

    	 //Poziva se prilikom otvaranja video toka.
      public void onPrepared(MediaPlayer mpl) { 
    	  text = (TextView) findViewById(R.id.textv);
    	  Toast.makeText(MainActivity.this, "Spajanje...", Toast.LENGTH_SHORT).show();
          mpl.start();

      }};
      
     MediaPlayer.OnErrorListener videoViewErrorListener
     = new MediaPlayer.OnErrorListener(){

    	 //Ako je došlo do greške, ispiši poruku.
      public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
    	  Toast.makeText(MainActivity.this,
          "Nema internet veze ili je kamera ugašena.",
          Toast.LENGTH_LONG).show();
          return true; }
      };
      
         //Postavljanje crne pozadine aplikacije
      public void setActivityBackgroundColor() {
    	    View view = this.getWindow().getDecorView();
    	    view.setBackgroundColor(16777216);
    	}
      
      //Funkcija koja poziva AsyncTask
      public void Detection(View view) {
    	  new MotionDetection(this, text).execute("http://kamerica.safe100.net:10000/cgi-bin/viewer/video.jpg");
          print("Uključena detekcija pokreta...");
      	}
      //Funkcije za rotaciju IP kamere
      public void move_up(View view) {
    	  WebView theWebPage = new WebView(this);
    	  theWebPage.loadUrl("http://kamerica.safe100.net:10000/cgi-bin/camctrl/camctrl.cgi?move=up");
      	  print("Pomicanje kamere prema gore...");
    	}
    	
      public void move_down(View view) {
    	  WebView theWebPage = new WebView(this);
    	  theWebPage.loadUrl("http://kamerica.safe100.net:10000/cgi-bin/camctrl/camctrl.cgi?move=down");
    	  print("Pomicanje kamere prema dolje...");
      	}
    
      public void move_left(View view) {
    	  WebView theWebPage = new WebView(this);
    	  theWebPage.loadUrl("http://kamerica.safe100.net:10000/cgi-bin/camctrl/camctrl.cgi?move=left");
    	  print("Pomicanje kamere ulijevo...");
      	}

      public void move_right(View view) {
    	  WebView theWebPage = new WebView(this);
    	  theWebPage.loadUrl("http://kamerica.safe100.net:10000/cgi-bin/camctrl/camctrl.cgi?move=right");
     	   print("Pomicanje kamere udesno...");
      	}
      
      // Funkcija koja poziva pokretanje video toka preko cijelog zaslona
      public void full(View view) {
    	  Intent intent = new Intent(Intent.ACTION_VIEW );
    	  intent.setDataAndType(Uri.parse(str), "video/*");
    	  startActivity(intent);
        }
    
      // Funckija koja ispisuje tekst na prikaz teksta
      public void print(final String s) {
    	text = (TextView) findViewById(R.id.textv);
    	text.setText("STATUS: " + s);
      	} 
}