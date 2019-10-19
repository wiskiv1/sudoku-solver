import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Sudoku_solver extends PApplet {

Space[][] grid = new Space[9][9];
boolean solving = false;

public void setup() {
  

  // creating all the spaces
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      grid[i][j] = new Space(j, i);
    }
  }
}

public void draw() {
  background(255);

  //drawing the squares
  for (Space[] row : grid) {
    for (Space collum : row) {
      collum.show();
    }
  }

  updateAll();
  
  //solving sudoku if enter solving == true
  if (solving) {
    for (Space[] row : grid) {
      for (Space collum : row) {
        updateAll();
        collum.check1();
        updateAll();
        collum.check2();
      }
    }
  }

  //check if sudoku is salved
  int SS = 0;
  for (Space[] row : grid) {
    for (Space collum : row) {
      if (collum.value != 0) {
        SS++;
      }
    }
  }
  if (SS == 81) {
    solving = false;
  }

  //some style thingy's
  push();
  stroke(0);
  strokeWeight(5);
  //vertical
  line(150, 0, 150, height);
  line(300, 0, 300, height);
  //horizontal
  line(0, 150, width, 150);
  line(0, 300, width, 300);
  pop();
}

public void mousePressed() {  
  for (Space[] row : grid) {
    for (Space collum : row) {
      collum.selected = false;
      collum.clicked();
    }
  }
}

public void keyPressed() {
  int newV = Character.getNumericValue(key);

  for (Space[] row : grid) {
    for (Space collum : row) {
      collum.change(newV);
    }
  }

  if (keyCode == ENTER) {
    solving = true;
  }
}


//update all possible options from all squares
public void updateAll() {
  for (Space[] row : grid) {
    for (Space collum : row) {
      collum.updateOptions();
    }
  }
}

class Space {

  boolean[] possibleOptions = new boolean[9];
  int value = 0;
  int posX, posY;
  boolean selected = false;

  Space(int x, int y) {
    posX = x;
    posY = y;

    for (int i = 0; i < 9; i++) {
      possibleOptions[i] = true;
    }
  }

  public void show() {
    push();
    //change collor if space is selcted
    if (selected) {
      fill(255, 255, 0);
    } else {
      fill(0, 0);
    }
    //draw square and the value of the square
    square(posX * 50, posY * 50, 50);
    fill(0);
    textAlign(CENTER);
    textSize(25);
    if (value != 0) {
      text(value, posX * 50 + 25, posY * 50 + 35);
    }
    pop();
  }

  //functions for solving the sudoku
  public void updateOptions() {
    //if value != 0 than change all possible options
    if (value != 0) {
      for (int i = 0; i < 9; i++) {
        possibleOptions[i] = false;
      }
      possibleOptions[value - 1] = true;
    }

    //check rows collums and sub grids to cross out options
    //collum
    for (int i = 0; i < 9; i++) {
      int spot = grid[posY][i].value;
      if (spot != 0 && i != posX) {
        possibleOptions[spot - 1] = false;
      }
    }

    //row
    for (int i = 0; i < 9; i++) {
      int spot = grid[i][posX].value;
      if (spot != 0 && i != posY) {
        possibleOptions[spot - 1] = false;
      }
    }

    //sub grid
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int spot = grid[floor(posY / 3) * 3 + j][floor(posX / 3) * 3 +i].value;
        if (spot != 0 && i != posX && j != posY) {
          possibleOptions[spot - 1] = false;
        }
      }
    }
  }


  public void check1() {
    int NpO = 0;
    int option = 0;
    for (int i = 0; i < 9; i++) {
      if (possibleOptions[i] == true) {
        NpO++;
        option = i + 1;
      }
    }

    if (NpO == 1) {
      value = option;
    }
  }


  public void check2() {
    //check collum for x
    for (int i = 0; i < 9; i++) {
      if (possibleOptions[i] == true) {
        if (hascollum(posX, posY, i)) {
          value = i + 1;
          return;
        }
      }
    }

    //check row for x
    for (int i = 0; i < 9; i++) {
      if (possibleOptions[i] == true) {
        if (hasrow(posX, posY, i)) {
          value = i + 1;
          return;
        }
      }
    }

    //check collum for x
    for (int i = 0; i < 9; i++) {
      if (possibleOptions[i] == true) {
        if (hassub(posX, posY, i)) {
          value = i + 1;
          return;
        }
      }
    }
  }


  //functions for munually changing sudoku value's
  public void clicked() {
    //check if this square is clicked && set selected to true
    int x = posX * 50;
    int y = posY * 50;
    if (mouseX >= x && mouseX <= x + 50 && mouseY >= y && mouseY <= y + 50) {
      selected = true;
    }
  }

  public void change(int newvalue) {
    if (selected && newvalue > 0) {
      value = newvalue;
    }
  }
}


//check if a row has a space with value x
public boolean hasrow(int posX, int posY, int x) {
  int NoF = 0;
  for (int i = 0; i < 9; i++) {
    boolean opt = grid[posY][i].possibleOptions[x];
    if (!opt && i != posX) {
      NoF++;
    }
  }
  if (NoF == 8) {
    return true;
  } else {
    return false;
  }
}

//check if a collum has a space with value x
public boolean hascollum(int posX, int posY, int x) {
  int NoF = 0;
  for (int i = 0; i < 9; i++) {
    boolean opt = grid[i][posX].possibleOptions[x];
    if (!opt && i != posY) {
      NoF++;
    }
  }
  if (NoF == 8) {
    return true;
  } else {
    return false;
  }
}

//check if a sub-grid has a space with value x
public boolean hassub(int posX, int posY, int x) {
  int NoF = 0;
  for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
      boolean opt = grid[floor(posY / 3) * 3 + j][floor(posX / 3) * 3 +i].possibleOptions[x];
      if (!opt && i != posX && j != posY) {
        NoF++;
      }
    }
  }
  if (NoF == 8) {
    return true;
  } else {
    return false;
  }
}

  public void settings() {  size(450, 450); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Sudoku_solver" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
