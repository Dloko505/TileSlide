package edu.cnm.deepdive.tileslide.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.*;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private static int PUZZLE_SIZE = 3;

  private Button newGame;
  private Button resetGame;
  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private boolean move;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tileGrid = findViewById(R.id.tile_grid);
    tileGrid.setNumColumns(PUZZLE_SIZE);
    createPuzzle();
    tileGrid.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         if (frame.move(position / PUZZLE_SIZE, position % PUZZLE_SIZE)) {
           adapter.notifyDataSetChanged();
         } else {
           Toast.makeText(getApplicationContext(), getString(R.string.illicit_move), Toast.LENGTH_LONG).show();
         }
      }
    });
    newGame = findViewById(R.id.new_game_button);
    newGame.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        createPuzzle();
        Toast.makeText(getApplicationContext(), R.string.game_new, Toast.LENGTH_SHORT).show();
      }
    });
    resetGame = findViewById(R.id.reset_game_button);
    resetGame.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        frame.reset();
        refresh();
        Toast.makeText(getApplicationContext(), R.string.game_reset, Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void createPuzzle() {
    frame = new Frame(PUZZLE_SIZE, new Random());
    refresh();
  }

  private void refresh() {
    adapter = new FrameAdapter(getApplicationContext(),frame);
    tileGrid.setAdapter(adapter);
  }

}
