package com.example.gleb.telegraph.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.filechooser.DirectoryFragment;
import com.example.gleb.telegraph.R;

public class FileChooserActivity extends ActionBarActivity {
	private Toolbar toolbar;
	private FragmentManager fragmentManager = null;
	private FragmentTransaction fragmentTransaction = null;
	private DirectoryFragment mDirectoryFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_chooser);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Directory");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        mDirectoryFragment=new DirectoryFragment();
        mDirectoryFragment.setDelegate(new DirectoryFragment.DocumentSelectActivityDelegate() {
			
			@Override
			public void startDocumentSelectActivity() {
				
			}
			
			@Override
			public void didSelectFiles(DirectoryFragment activity,
					ArrayList<String> files) {
//				mDirectoryFragment.showErrorBox(files.get(0).toString());
				Intent intent = new Intent();
				intent.putExtra("PathFile", files.get(0).toString());
				setResult(RESULT_OK, intent);
				finish();
			}

			@Override
			public void updateToolBarName(String name) {
				toolbar.setTitle(name);
				
			}
		});
        fragmentTransaction.add(R.id.fragment_container, mDirectoryFragment,
				"" + mDirectoryFragment.toString());
		fragmentTransaction.commit();
        
	}

	@Override
	protected void onDestroy() {
		mDirectoryFragment.onFragmentDestroy();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (mDirectoryFragment.onBackPressed_()) {
			super.onBackPressed();
		}
	}
	
}
