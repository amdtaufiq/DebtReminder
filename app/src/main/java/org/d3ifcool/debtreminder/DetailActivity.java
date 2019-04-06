package org.d3ifcool.debtreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.PembayaranListAdapter;
import adapter.PeminjamanListAdapter;
import database.DBHelper;
import entities.Pembayaran;
import entities.Peminjaman;
import function.FunctionPembayaran;
import function.FunctionPeminjaman;

public class DetailActivity extends AppCompatActivity {

    private TextView mAmount;
    private TextView mName;
    private TextView mPhone;
    private TextView mDescription;
    private TextView mDateOfLand;
    private TextView mDateDue;

    private Button mBtnDetailDelete;
    private Button mBtnDetailUpdate;
    private Button mBtnDetailPay;

    private RecyclerView recyclerView;

    private DBHelper mDbHelper = new DBHelper(this);
    private Cursor mCursor;
    private int mPayment;
    private TextView mTvPayment;



    @Override
    protected void onStart() {
        super.onStart();
        Peminjaman peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        sqLiteDatabase.isOpen();

        mCursor = sqLiteDatabase.rawQuery("SELECT SUM(pay) FROM pembayaran WHERE idPeminjaman = ?",new String[]{String.valueOf(peminjaman.getIdPeminjaman())});
        if (mCursor.moveToFirst()){
            mPayment = mCursor.getInt(0);
        }else {
            mPayment = -1;
        }
        mCursor.close();

        mTvPayment = findViewById(R.id.tv_payment);
        mTvPayment.setText("Total : "+mPayment);


        mAmount = findViewById(R.id.tv_detail_amount);
        mAmount.setText(String.valueOf(peminjaman.getAmount()));
        mName = findViewById(R.id.tv_detail_name);
        mName.setText(peminjaman.getName());
        mPhone = findViewById(R.id.tv_detail_telphon);
        mPhone.setText(peminjaman.getTelphon());
        mDescription = findViewById(R.id.tv_detail_description);
        mDescription.setText(peminjaman.getDescription());
        mDateOfLand = findViewById(R.id.tv_detail_dl);
        mDateOfLand.setText(peminjaman.getDateOfLoan());
        mDateDue = findViewById(R.id.tv_detail_dd);
        mDateDue.setText(peminjaman.getDateDue());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Peminjaman peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");

        mBtnDetailDelete = findViewById(R.id.btn_detail_delete);
        mBtnDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(false);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FunctionPeminjaman functionPeminjaman = new FunctionPeminjaman(getBaseContext());
                        if (functionPeminjaman.delete(peminjaman.getIdPeminjaman())){
                            Intent intentDelete = new Intent(DetailActivity.this,MainActivity.class);
                            startActivity(intentDelete);
                        }else {
                            AlertDialog.Builder builderDeleteInformation = new AlertDialog.Builder(getBaseContext());
                            builderDeleteInformation.setCancelable(false);
                            builderDeleteInformation.setMessage("Fail");
                            builderDeleteInformation.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builderDeleteInformation.create().show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        mBtnDetailUpdate = findViewById(R.id.btn_detail_update);
        mBtnDetailUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEdit = new Intent(DetailActivity.this, EditPeminjamanActivity.class);
                intentEdit.putExtra("peminjaman",peminjaman);
                startActivity(intentEdit);
            }
        });

        mBtnDetailPay = findViewById(R.id.btn_detail_pay);
        mBtnDetailPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPay = new Intent(DetailActivity.this, AddPembayaranActivity.class);
                intentPay.putExtra("peminjaman",peminjaman);
                startActivity(intentPay);
            }
        });


        recyclerView = findViewById(R.id.rv_payment);
        FunctionPembayaran functionPembayaran =  new FunctionPembayaran(this);
        ArrayList<Pembayaran> listPembayaran = new ArrayList<>();
        listPembayaran.addAll(functionPembayaran.findIdPeminjaman(peminjaman.getIdPeminjaman()));
        PembayaranListAdapter adapter = new PembayaranListAdapter(this,listPembayaran);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(this,layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
    }
}
