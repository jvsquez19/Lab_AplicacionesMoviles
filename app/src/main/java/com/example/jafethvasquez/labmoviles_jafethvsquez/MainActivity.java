package com.example.jafethvasquez.labmoviles_jafethvsquez;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private static final int PHOTO_CAPTURE = 101;

    Button save = null;
    Button addPicture = null;
    EditText nameText = null;
    EditText professionText = null;
    RadioButton maleButton = null;
    RadioButton femaleButton = null;
    ListView peopleList = null;
    private ArrayList<String> matchesText;
    private ArrayList<Person> p_arrayList = new ArrayList<Person>();
    private Dialog matchTextDialog;
    String picture = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.save = (Button)findViewById(R.id.save);
        this.addPicture = (Button)findViewById(R.id.addPicture);
        this.nameText = (EditText)findViewById(R.id.name);
        this.professionText = (EditText)findViewById(R.id.profession);
        this.maleButton = (RadioButton)findViewById(R.id.male);
        this.femaleButton = (RadioButton)findViewById(R.id.female);
        this.peopleList = findViewById(R.id.peopleList);

        if(!hasCamera()){
            addPicture.setEnabled(false);
        }

        professionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    Intent intent = new
                            Intent ( RecognizerIntent.ACTION_RECOGNIZE_SPEECH ) ;
                    intent . putExtra ( RecognizerIntent . EXTRA_LANGUAGE_MODEL ,
                            RecognizerIntent . LANGUAGE_MODEL_FREE_FORM ) ;
                    startActivityForResult ( intent , REQUEST_CODE ) ;


                }
                else{
                    Toast. makeText ( getApplicationContext () , "Please Connect To Internet" , Toast . LENGTH_SHORT ) . show ();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameText.getText().toString().equals("") || professionText.getText().toString().equals("") || professionText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "No se han llenado todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    String gen = "Female";
                    if (maleButton.isChecked()) {
                        gen = "Male";
                    }


                    Person newPerson = new Person(
                    nameText.getText().toString(),
                    professionText.getText().toString(),
                            picture,gen);

                    p_arrayList.add(newPerson);

                    peopleList.setAdapter(new viewAdapter(MainActivity.this));
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent
            data ){
        super . onActivityResult ( requestCode , resultCode , data );
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            matchTextDialog = new Dialog( MainActivity . this );
            matchTextDialog.setContentView(R.layout.dialog_matches_frag);
            ListView textListView = (ListView) matchTextDialog.findViewById(R.id.listView1);
            matchTextDialog . setTitle (" Say Your Proffesional Profile ");
            matchesText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matchesText);
            textListView.setAdapter(adapter);
            textListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    professionText.setText(matchesText.get(i));
                    matchTextDialog.hide();
                }
            });
            matchTextDialog.show();
        }

        Uri videoUri = data.getData();

        if(requestCode == PHOTO_CAPTURE && resultCode == RESULT_OK){
            Toast.makeText (this , " Video saved to :\n" +
                    videoUri, Toast.LENGTH_LONG).show ();
            picture = videoUri.toString();
        }
        else if ( resultCode == RESULT_CANCELED ) {
            Toast . makeText (this , " Video recording cancelled .",
                    Toast . LENGTH_LONG ) . show () ;
        }
        else {
            Toast.makeText(this , " Failed to record video ",
                    Toast.LENGTH_LONG ).show () ;
        }

    }


    public void startRecording ( View view )
    {
        Intent intent = new Intent ( MediaStore. ACTION_IMAGE_CAPTURE ) ;
        startActivityForResult ( intent , PHOTO_CAPTURE ) ;
    }

    private boolean hasCamera () {
        return ( getPackageManager () . hasSystemFeature (
                PackageManager. FEATURE_CAMERA_ANY ) ) ;
    }







    public boolean isConnected () {
        ConnectivityManager cm = ( ConnectivityManager )
                getSystemService ( Context.CONNECTIVITY_SERVICE ) ;
        NetworkInfo net = cm.getActiveNetworkInfo () ;
        if ( net != null && net.isAvailable() && net.isConnected() ) {
            return true ;
        } else {
            return false ;
        }
    }


    //Adapter que Infla el ListView
    public class viewAdapter extends BaseAdapter {
        LayoutInflater mInflater;
        public viewAdapter(Context context){
            mInflater= LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return p_arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return p_arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return  i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            if(view==null){
                view=mInflater.inflate(R.layout.list_item,null);
            }
            final TextView txt_nombre= (TextView) view.findViewById(R.id.item_nombre);
            TextView txt_perfil= (TextView) view.findViewById(R.id.item_perfil);
            TextView txt_gen= (TextView) view.findViewById(R.id.item_gen);
            ImageView imagen= (ImageView) view.findViewById(R.id.item_img);


            txt_nombre.setText(p_arrayList.get(i).getName());
            txt_perfil.setText(p_arrayList.get(i).getProfession());
            txt_gen.setText(p_arrayList.get(i).getGender());
            imagen.setImageURI(Uri.parse(p_arrayList.get(i).getPicture()));
            return view;

        }
    }



}
