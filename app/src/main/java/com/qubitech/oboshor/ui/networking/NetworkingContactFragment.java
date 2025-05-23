package com.qubitech.oboshor.ui.networking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.HubDataModel;
import com.qubitech.oboshor.datamodels.UserDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NetworkingContactFragment extends Fragment {


    private View root;
    private EditText nameText, emailText, subjectText, messageText;
    private Button sendBtn;
    private RecyclerView hubsRecycler;
    private ArrayList<HubDataModel> hubDataModels;
    private HubsAdapter hubsAdapter;

    public NetworkingContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(root==null)
            root = inflater.inflate(R.layout.fragment_networking_contacts, container, false);

        nameText = root.findViewById(R.id.contact_us_name);
        emailText = root.findViewById(R.id.contact_us_email);
        subjectText = root.findViewById(R.id.contact_us_email_subject);
        messageText = root.findViewById(R.id.contact_us_email_message);
        sendBtn = root.findViewById(R.id.contact_us_send_btn);
        hubsRecycler = root.findViewById(R.id.contact_us_hubs_recyclerview);

        hubDataModels = new ArrayList<>();

        hubsAdapter = new HubsAdapter(getActivity(), hubDataModels);
        hubsRecycler.setAdapter(hubsAdapter);
        hubsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



        Intent mapsIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir//24.3745783,88.6225603"));
        hubDataModels.add(new HubDataModel("Vodra, Rajshahi","Vodra Bus Stand, Station Road, Rajshahi",mapsIntent1));

        Intent mapsIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir//24.3620445,88.625677"));
        hubDataModels.add(new HubDataModel("Talaimari, Rajshahi","Talaimari, Near RUET, Rajshahi",mapsIntent2));

        Intent mapsIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir//24.3642304,88.6343796"));
        hubDataModels.add(new HubDataModel("Kajla, Rajshahi","Kajla, Near RU, Rajshahi",mapsIntent3));

        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+8801793343203"));
        hubDataModels.add(new HubDataModel("+ 88-01793-343203", "Call Us Now", callIntent));

        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/obosor.books"));
        hubDataModels.add(new HubDataModel("facebook.com/Obosor.Books","Facebook",facebookIntent));

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"connect.obosorbooks@gmail.com"});
        hubDataModels.add(new HubDataModel("connect.obosorbooks@gmail.com","Gmail",emailIntent));

        Intent instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/obosor.bookss"));
        hubDataModels.add(new HubDataModel("obosor.bookss","Instagram",instagramIntent));
        hubsAdapter.setHubDataModels(hubDataModels);



        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmail();
            }
        });
    }


    private void verifyEmail(){

        String name="", email="", subject="", message="";


        if(nameText.getText().toString().isEmpty()){

            nameText.setError("Name required");
            nameText.requestFocus();

        }
        else{
            name = nameText.getText().toString();
        }

        if(emailText.getText().toString().isEmpty()){

            emailText.setError("Email ID required");
            emailText.requestFocus();

        }
        else{
            email = emailText.getText().toString();
        }

        if(subjectText.getText().toString().isEmpty()){

            subjectText.setError("Email Subject required");
            subjectText.requestFocus();

        }
        else{
            subject = subjectText.getText().toString();
        }

        if(messageText.getText().toString().isEmpty()){

            messageText.setError("Email Body required");
            messageText.requestFocus();

        }
        else{
            message = messageText.getText().toString();
        }

        if(!name.isEmpty() && !email.isEmpty() && !subject.isEmpty() && !message.isEmpty()){

            message = "Email from: "+email+System.lineSeparator()+System.lineSeparator()+message+System.lineSeparator()+System.lineSeparator()+"Sincerely,"+System.lineSeparator()+name+System.lineSeparator();
            composeEmail(subject, message);

        }

    }

    public void composeEmail(String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"connect.obosorbooks@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);

    }
}
