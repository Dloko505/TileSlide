package edu.cnm.deepdive.tileslide.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.*;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.model.Tile;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  public static final String TILE_NUMS_KEY = "tileNums";
  public static final String START_NUMS_KEY = "startNums";
  private static final int REQUEST_CODE = 1;
  private static int PUZZLE_SIZE = 4;

  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private TextView movesMade;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    movesMade = findViewById(R.id.moves_made);
    tileGrid = findViewById(R.id.tile_grid);

    tileGrid.setNumColumns(PUZZLE_SIZE);
    createPuzzle(savedInstanceState);
    updateMoves();

    tileGrid.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (frame.move(position / PUZZLE_SIZE, position % PUZZLE_SIZE)) {
          adapter.notifyDataSetChanged();
          updateMoves();
          if (frame.isWin()) {
            tileGrid.setEnabled(false);
            Toast.makeText(getApplicationContext(), R.string.you_won, Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(getApplicationContext(), R.string.illicit_move, Toast.LENGTH_LONG).show();
        }
      }
    });
    Button newGame = findViewById(R.id.new_game_button);
    newGame.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        createPuzzle(savedInstanceState);
        Toast.makeText(getApplicationContext(), R.string.game_new, Toast.LENGTH_SHORT).show();
      }
    });
    Button resetGame = findViewById(R.id.reset_game_button);
    resetGame.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        frame.reset();
        updateMoves();
        refresh();
        Toast.makeText(getApplicationContext(), R.string.game_reset, Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void createPuzzle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      frame = new Frame(
          PUZZLE_SIZE,
          // frame.tiles[][]
          savedInstanceState
              .getIntegerArrayList(TILE_NUMS_KEY)
              .toArray(new Integer[0]),
          // frame.start[][]
          savedInstanceState.getIntegerArrayList(START_NUMS_KEY)
              .toArray(new Integer[0]),
          savedInstanceState.getInt("SAVE_MOVES"),
          new Random()
      );
    } else {
      frame = new Frame(PUZZLE_SIZE, new Random());
    }
    setAdapter();
  }

  public void pickImage(View view) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, REQUEST_CODE);
  }



  private void refresh() {
    setAdapter();
    tileGrid.setAdapter(adapter);
  }

  private void setAdapter() {
    adapter = new FrameAdapter(this, frame);
    tileGrid.setAdapter(adapter);
  }
  private void updateMoves() {
    movesMade.setText(String.format(getString(R.string.moves_made), frame.getMoves()));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    ArrayList<Integer> tileNums = new ArrayList<>();
    Tile[][] source = frame.getTiles();
    for (int row = 0; row < source.length; row++) {
      for (int col = 0; col < source[0].length; col++) {
        tileNums.add((source[row][col] == null)?
            null : (source[row][col].getNumber()));
      }
    }
    ArrayList<Integer> startNums = new ArrayList<>();
    Tile[][] start = frame.getStart();
    for (int row = 0; row < start.length; row++) {
      for (int col = 0; col < start[0].length; col++) {
        startNums.add((start[row][col] == null)?
            null : (start[row][col].getNumber()));
      }
    }
    outState.putIntegerArrayList(TILE_NUMS_KEY, tileNums);
    outState.putIntegerArrayList(START_NUMS_KEY, startNums);
    outState.putInt("SAVE_MOVES", frame.getMoves());
    super.onSaveInstanceState(outState);
  }
}