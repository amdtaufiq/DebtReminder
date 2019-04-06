package org.d3ifcool.debtreminder;

import android.annotation.SuppressLint;
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
import java.util.Calendar;

import entities.Peminjaman;
import fragment.DatePickerFragment;
import function.FunctionPeminjaman;

public class EditPeminjamanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText mName;
    private EditText mPhone;
    private EditText mAmount;
    private EditText mDescription;
    private TextView mDd;
    private Button mBtnDateDue;
    private Button mBtnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_peminjaman);


        final Intent intent = getIntent();
        final Peminjaman peminjaman = (Peminjaman) intent.getSerializableExtra("peminjaman");

        mName = findViewById(R.id.et_name);
        mName.setText(peminjaman.getName());

        mPhone = findViewById(R.id.et_phone);
        mPhone.setText(peminjaman.getTelphon());

        mAmount = findViewById(R.id.et_amount);
        mAmount.setText(String.valueOf(peminjaman.getAmount()));

        mDescription = findViewById(R.id.et_description);
        mDescription.setText(peminjaman.getDescription());

        mDd = findViewById(R.id.hiden);
        mDd.setText(peminjaman.getDateDue());

        mBtnDateDue = findViewById(R.id.btn_d3);
        mBtnDateDue.setText(peminjaman.getDateDue());
        mBtnDateDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        mBtnEdit = findViewById(R.id.button_edit);
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionPeminjaman functionPeminjaman = new FunctionPeminjaman(getBaseContext());
                peminjaman.setName(mName.getText().toString().trim());
                peminjaman.setTelphon(mPhone.getText().toString().trim());
                peminjaman.setAmount(Integer.parseInt(mAmount.getText().toString().trim()));
                peminjaman.setDescription(mDescription.getText().toString().trim());
                peminjaman.setDateDue(mDd.getText().toString().trim());


//                if (mName.getText().toString().equals("") ||
//                        mPhone.getText().toString().equals("") ||
//                        mAmount.getText().toString().equals("") ||
//                        mDescription.getText().toString().equals("")){
//                    Toast.makeText(EditPeminjamanActivity.this, "Masih Ada Data Yang Kosong", Toast.LENGTH_SHORT).show();
//                }else
                    if (functionPeminjaman.Update(peminjaman)){
                    Intent intentUpdate = new Intent(EditPeminjamanActivity.this, DetailActivity.class);
                    intentUpdate.putExtra("peminjaman",peminjaman);
                    Toast.makeText(EditPeminjamanActivity.this, "Data berhasil diedit", Toast.LENGTH_SHORT).show();
                    startActivity(intentUpdate);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("fail");
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
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        mBtnDateDue.setText(currentDateString);
        mDd.setText(currentDateString);
    }

}
