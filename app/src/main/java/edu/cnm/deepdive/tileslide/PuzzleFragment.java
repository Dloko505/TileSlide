package edu.cnm.deepdive.tileslide;



import static edu.cnm.deepdive.tileslide.PuzzleFragment.IMAGE_SELECT_KEY;
import static edu.cnm.deepdive.tileslide.PuzzleFragment.LEVEL_SELECT_KEY;
import static edu.cnm.deepdive.tileslide.controller.MainActivity.START_NUMS_KEY;
import static edu.cnm.deepdive.tileslide.controller.MainActivity.TILE_NUMS_KEY;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.model.Tile;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PuzzleFragment extends Fragment {

  public static int PUZZLE_SIZE = 3;
  public static int puzzleImage;
  private static Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private TextView movesMade;
  public static final String IMAGE_SELECT_KEY = "puzzle_image";


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    createPuzzle(savedInstanceState);

  }

  public PuzzleFragment() {

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      final Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.activity_main, container, false);

    movesMade = view.findViewById(R.id.moves_made);
    tileGrid = view.findViewById(R.id.tile_grid);

    tileGrid.setNumColumns(PUZZLE_SIZE);
    createPuzzle(savedInstanceState);
    updateMoves();

    tileGrid.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (frame.move(position / PUZZLE_SIZE, position % PUZZLE_SIZE)) {
          adapter.notifyDataSetChanged();
          updateMoves();
        } else {
          Toast.makeText(getActivity(), R.string.illicit_move, Toast.LENGTH_LONG).show();
        }
      }
    });
    Button newGame = view.findViewById(R.id.new_game_button);
    newGame.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        createPuzzle(savedInstanceState);
        Toast.makeText(getActivity(), R.string.game_new, Toast.LENGTH_SHORT).show();
      }
    });
    Button resetGame = view.findViewById(R.id.reset_game_button);
    resetGame.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        frame.reset();
        updateMoves();
        refresh();
        Toast.makeText(getActivity(), R.string.game_reset, Toast.LENGTH_SHORT).show();
      }
    });
    return view;
  }

  public void createPuzzle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      frame = new Frame(
          PUZZLE_SIZE,
          savedInstanceState
              .getIntegerArrayList(TILE_NUMS_KEY)
              .toArray(new Integer[0]),
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

  public void refresh() {
    setAdapter();
    tileGrid.setAdapter(adapter);
  }

  private void setAdapter() {
    adapter = new FrameAdapter(getActivity(), frame);
    tileGrid.setAdapter(adapter);
  }

  public void updateMoves() {
    movesMade.setText(String.format(getString(R.string.moves_made), frame.getMoves()));
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
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


class PreferenceChangeListener implements OnSharedPreferenceChangeListener {


  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    
      if (key.equals(IMAGE_SELECT_KEY)) {
      switch (Objects.requireNonNull(sharedPreferences.getString(key, null))) {
        case "Joe Schmo":
          PuzzleFragment.puzzleImage = R.drawable.android_robot_circle;
          break;
        case "Pirate":
          PuzzleFragment.puzzleImage = R.drawable.android_pirate;
          break;
        case "Zombie":
          PuzzleFragment.puzzleImage = R.drawable.android_zombie;
          break;

      }
    }

  }

}


