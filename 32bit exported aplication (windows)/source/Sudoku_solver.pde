//made by wiskiv1
//github.com/wiskiv1/sudoku-solver

Space[][] grid = new Space[9][9];
boolean solving = false;

//brute forcing stuff when it's stuck
Space[][] savepoint = new Space[9][9];
int protection = 0;
boolean stuck = false;

void setup() {
  size(450, 450);

  // creating all the spaces
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      grid[i][j] = new Space(j, i);
    }
  }
  
  // creating all the savepoints
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      savepoint[i][j] = new Space(j, i);
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
  if (solving && protection < 50) {
    for (Space[] row : grid) {
      for (Space collum : row) {
        updateAll();
        collum.check1();
        updateAll();
        collum.check2();
      }
    }
    protection++;
  }

  //when protection >= 1000 insert a random value in a square that doesn't have a value jet
  if (protection >= 50 && stuck == false) {
    gridSave();
    fillrandom();
    protection = 0;
    stuck = true;
  } else if (protection >= 50 && stuck == true) {
    saveGrid();
    fillrandom();
    protection = 0;
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

//functions to copy grid to savepoint and vice versa
void gridSave() {
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      savepoint[i][j].kopieer(grid[i][j]);
    }
  }
}

void saveGrid() {
  for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
      grid[i][j].kopieer(savepoint[i][j]);
    }
  }
}


//function to insert random number in empty spot
void fillrandom() {
  int x = floor(random(0,9));
  int y = floor(random(0,9));
  
  if (grid[y][x].value == 0) {
    while (grid[y][x].value == 0) {
      int nv = floor(random(0,9));
      if (grid[y][x].possibleOptions[nv] == true) {
        grid[y][x].value = nv + 1;
      }
    }
  }
}
