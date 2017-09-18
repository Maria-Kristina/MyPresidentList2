package com.example.mypresidentlist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.net.Uri;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by M-K on 23.8.2017.
 */

public class President {
    String firstName;
    String lastName;
    int aloitusVuosi;
    int lopetusVuosi;
    String detail;

    public President(String firstName, String lastName, int aloitusVuosi, int lopetusVuosi, String detail){
        this.firstName = firstName;
        this.lastName = lastName;
        this.aloitusVuosi = aloitusVuosi;
        this.lopetusVuosi = lopetusVuosi;
        this.detail = detail;


    }


    @Override
    public String toString() {

        return lastName + ", " + firstName + " " + aloitusVuosi + " - " + lopetusVuosi;

    }


}
