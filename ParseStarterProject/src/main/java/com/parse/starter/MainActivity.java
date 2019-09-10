/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import javax.xml.datatype.Duration;


public class MainActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button login = (Button) findViewById(R.id.loginButton);
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String[] input = getCredentialInput();
        if (input != null) {
          ParseUser.logInInBackground(input[0], input[1], new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
              if (user != null) {
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
              } else {
                switch (e.getCode()) {
                    case ParseException.OBJECT_NOT_FOUND:
                      showSignUpButton();
                      break;

                }

                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), String.valueOf(e.getCode()), Toast.LENGTH_LONG).show();
              }
            }
          });
        }
      }
    });

    Button signUp = (Button) findViewById(R.id.signUpButton);
    signUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String[] input = getCredentialInput();
        if (input != null) {
          ParseUser user = new ParseUser();
          user.setUsername(input[0]);
          user.setPassword(input[1]);

          user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
              if (e == null) {
                Toast.makeText(getApplicationContext(), "Registered Success", Toast.LENGTH_SHORT).show();
              } else {
                switch (e.getCode()) {
                  case ParseException.USERNAME_TAKEN:
//                    hideSignUpButton();
                    break;
                }
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), String.valueOf(e.getCode()), Toast.LENGTH_LONG).show();
              }
            }
          });
        }
      }
    });

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  private String[] getCredentialInput() {
    String username = ((TextView) findViewById(R.id.usernameEditText)).getText().toString();
    String password = ((TextView) findViewById(R.id.passwordEditText)).getText().toString();

    if (!username.equals("") && !password.equals("")) {
      return new String[] {username, password};
    } else {
      Toast.makeText(getApplicationContext(), "Please enter valid information", Toast.LENGTH_LONG).show();
      return null;
    }
  }

  private void hideSignUpButton() {
    final Button login = (Button) findViewById(R.id.loginButton);
    final Button signUp = (Button) findViewById(R.id.signUpButton);

    signUp.animate().alpha(0).setDuration(500).withEndAction(new Runnable() {
      @Override
      public void run() {
        login.animate().translationXBy(150f).setDuration(500).start();
        signUp.setVisibility(View.INVISIBLE);
      }
    }).start();

  }

  private void showSignUpButton() {
    Button login = (Button) findViewById(R.id.loginButton);
    final Button signUp = (Button) findViewById(R.id.signUpButton);

    login.animate().translationX(-200).setDuration(500).withEndAction(new Runnable() {
      @Override
      public void run() {
        signUp.animate().alpha(100).setDuration(500).start();
        signUp.setVisibility(View.VISIBLE);
      }
    }).start();

  }

}