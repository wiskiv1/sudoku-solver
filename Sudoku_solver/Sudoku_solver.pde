//made by wiskiv1
//github.com/wiskiv1/sudoku-solver

Space[][] grid = new Space[9][9];
boolean solving = false;

void setup() {
  size(450, 450);

  // creating all the spaces
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      grid[i][j] = new Space(j, i);
    }
  }
}

void draw() {
  background(255);

  //drawing the squares
  for (Space[] row : grid) {
    for (Space collum : row) {
      collum.show();
    }
  }
  
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

  //check if sudoku is solved but not really
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

void mousePressed() {  
  for (Space[] row : grid) {
    for (Space collum : row) {
      collum.selected = false;
      collum.clicked();
    }
  }
}

void keyPressed() {
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
void updateAll() {
  for (Space[] row : grid) {
    for (Space collum : row) {
      collum.updateOptions();
    }
  }
}
