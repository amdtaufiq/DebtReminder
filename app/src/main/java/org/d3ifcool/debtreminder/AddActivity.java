package org.d3ifcool.debtreminder;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import entities.Peminjaman;
import fragment.DatePickerFragment;
import function.FunctionPeminjaman;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextAmount;
    private EditText editTextDescription;
    private TextView textViewD3;
    private Button buttonAddAmount;
    private Button buttonAddD3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextName = findViewById(R.id.edit_text_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextAmount = findViewById(R.id.edit_text_amount);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewD3 = findViewById(R.id.textViewD3);
        textViewD3.setVisibility(View.GONE);
//        editTextD3 = findViewById(R.id.edit_text_d3);


        buttonAddD3 = findViewById(R.id.button_d3);
        buttonAddD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        buttonAddAmount = findViewById(R.id.button_hutang);
        buttonAddAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionPeminjaman functionPeminjaman = new FunctionPeminjaman(getBaseContext());
                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setName(editTextName.getText().toString().trim());
                peminjaman.setTelphon(editTextPhone.getText().toString().trim());
                peminjaman.setAmount(Integer.parseInt(editTextAmount.getText().toString().trim()));
                peminjaman.setDescription(editTextDescription.getText().toString().trim());
                peminjaman.setDateDue(textViewD3.getText().toString().trim());


                if (editTextName.getText().toString().equals("") ||
                        editTextPhone.getText().toString().equals("") ||
                        editTextAmount.getText().toString().equals("") ||
                        editTextDescription.getText().toString().equals("")){
                    Toast.makeText(AddActivity.this, "Masih Ada Data Yang Kosong", Toast.LENGTH_SHORT).show();
                }else if (functionPeminjaman.create(peminjaman)){
                    Intent intentAdd = new Intent(AddActivity.this, MainActivity.class);
                    Toast.makeText(AddActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    startActivity(intentAdd);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Fail DB");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        buttonAddD3.setText(currentDateString);
        textViewD3.setText(currentDateString);
//        Peminjaman peminjaman = new Peminjaman();
//        peminjaman.setDateDue(currentDateString);
    }

    private void insert(){
        FunctionPeminjaman functionPeminjaman = new FunctionPeminjaman(getBaseContext());
        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setTelphon(editTextPhone.getText().toString());
    }
}
