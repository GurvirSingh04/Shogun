// Main Part 4 about the Shogun Board Game
//=========================================

// Task 1 - 5 see below

object M4 {   

type Pos = (Int, Int)    // a position on a chessboard 

// Colours: Red or White
abstract class Colour
case object Red extends Colour
case object Wht extends Colour

// Pieces: Either Pawns or Kings
//===============================
abstract class Piece {
  def pos : Pos       
  def col : Colour    
  def en : Int      // energy for Pawns 1 - 4, for Kings 1 - 2
}
case class Pawn(en: Int, col: Colour, pos: Pos) extends Piece
case class King(en: Int, col: Colour, pos: Pos) extends Piece

// how to extract components from pieces
//val p = Pawn(4, Wht, (3,2))
//assert(p.pos == (3,2))
//assert(p.col == Wht)
//assert(p.en == 4)  

// checks if a piece is a king
def is_king(pc: Piece) : Boolean = pc match {
  case King(_, _, _) => true
  case _ => false
}

// incrementing and decrementing the position of a piece
def incx(pc: Piece) : Piece = pc match {
  case Pawn(en, c, (x,y)) => Pawn(en, c, (x+1,y))
  case King(en, c, (x,y)) => King(en, c, (x+1,y))
}

def incy(pc: Piece) : Piece = pc match {
  case Pawn(en, c, (x,y)) => Pawn(en, c, (x,y+1))
  case King(en, c, (x,y)) => King(en, c, (x,y+1))
}

def decx(pc: Piece) : Piece = pc match {
  case Pawn(en, c, (x,y)) => Pawn(en, c, (x-1,y))
  case King(en, c, (x,y)) => King(en, c, (x-1,y))
}

def decy(pc: Piece) : Piece = pc match {
  case Pawn(en, c, (x,y)) => Pawn(en, c, (x,y-1))
  case King(en, c, (x,y)) => King(en, c, (x,y-1))
}

//pretty printing colours and pieces
def pp_color(c: Colour) : String = c match {
  case Red => "R"
  case Wht => "W"
}

def pp(pc: Piece) : String = pc match {
  case Pawn(n, c, _) => s"P${pp_color(c)}$n"
  case King(n, c, _) => s"K${pp_color(c)}$n"
}

// Boards are sets of pieces
//===========================
case class Board(pces: Set[Piece]) {
  def +(pc: Piece) : Board = Board(pces + pc)
  def -(pc: Piece) : Board = Board(pces - pc)
}

// checking whether a position is occupied in a board
def occupied(p: Pos, b: Board) : Option[Piece] =  
  b.pces.find(p == _.pos)
  
def occupied_by(p: Pos, b: Board) : Option[Colour] =
  occupied(p, b).map(_.col)

def is_occupied(p: Pos, b: Board) : Boolean =
  occupied(p, b).isDefined

// is a position inside a board
def inside(p: Pos, b: Board): Boolean = 
  1 <= p._1 && 1 <= p._2 && p._1 <= 8 && p._2 <= 8 

// pretty printing a board
def print_board(b: Board): Unit = {
  println()
  for (i <- 8 to 1 by -1) {
    println("----" * 8)
    for (j <- 1 to 8) {
      val opc = occupied((j,i), b)
      if (opc.isDefined) print(s"|${pp(opc.get)}") 
      else print("|   ")
    }
    println("|")
  } 
  println("----" * 8)
}

// example board: initial board
val b_init = Board(Set(King(2,Wht,(4,1)), King(1,Red,(5,8)),
                  		 Pawn(4,Wht,(1,1)), Pawn(4,Red,(1,8)),
                  		 Pawn(3,Wht,(2,1)), Pawn(2,Red,(2,8)),
                  		 Pawn(2,Wht,(3,1)), Pawn(3,Red,(3,8)),
                  		 Pawn(1,Wht,(5,1)), Pawn(1,Red,(4,8)),
                  		 Pawn(4,Wht,(6,1)), Pawn(3,Red,(6,8)),
                  		 Pawn(3,Wht,(7,1)), Pawn(1,Red,(7,8)),
                  		 Pawn(2,Wht,(8,1)), Pawn(3,Red,(8,8))))

//print_board(b_init)
// --------------------------------
// |PR4|PR2|PR3|PR1|KR1|PR3|PR1|PR3|
// --------------------------------
// |   |   |   |   |   |   |   |   |
// --------------------------------
// |   |   |   |   |   |   |   |   |
// --------------------------------
// |   |   |   |   |   |   |   |   |
// --------------------------------
// |   |   |   |   |   |   |   |   |
// --------------------------------
// |   |   |   |   |   |   |   |   |
// --------------------------------
// |   |   |   |   |   |   |   |   |
// --------------------------------
// |PW4|PW3|PW2|KW2|PW1|PW4|PW3|PW2|
// --------------------------------




// Moves
//=======
abstract class Move
case object U extends Move    // up
case object D extends Move    // down
case object R extends Move    // right
case object L extends Move    // left
case object RU extends Move   // first right, then possibly up
case object LU extends Move   // first left, then possibly up
case object RD extends Move   // ...
case object LD extends Move
case object UR extends Move
case object UL extends Move
case object DR extends Move
case object DL extends Move


def eval(pc: Piece, m: Move, en: Int, b: Board): Set[Piece] = {
  if (!inside(pc.pos, b)) {
    Set()
  } else if (en == 0 && !is_occupied(pc.pos, b)) {
    Set(pc)
  } else if (en == 0 && is_occupied(pc.pos, b)) {
    if (occupied_by(pc.pos, b).get != pc.col) {
      Set(pc)
    } else {
      Set()
    }
  } else if (en > 0 && is_occupied(pc.pos, b)) {
    Set()
  } else {
    m match {
      case U => eval(incy(pc), U, en - 1, b)
      case D => eval(decy(pc), D, en - 1, b)
      case L => eval(decx(pc), L, en - 1, b)
      case R => eval(incx(pc), R, en - 1, b)
      case RU => eval(incx(pc), RU, en - 1, b) ++ eval(pc, U, en, b)
      case LU => eval(decx(pc), LU, en - 1, b) ++ eval(pc, U, en, b)
      case RD => eval(incx(pc), RD, en - 1, b) ++ eval(pc, D, en, b)
      case LD => eval(decx(pc), LD, en - 1, b) ++ eval(pc, D, en, b)
      case UR => eval(incy(pc), UR, en - 1, b) ++ eval(pc, R, en, b)
      case UL => eval(incy(pc), UL, en - 1, b) ++ eval(pc, L, en, b)
      case DR => eval(decy(pc), DR, en - 1, b) ++ eval(pc, R, en, b)
      case DL => eval(decy(pc), DL, en - 1, b) ++ eval(pc, L, en, b)
    }
  }
}

def all_moves(pc: Piece, b: Board): Set[Piece] = {
  val possibleMoves = List(U, D, L, R, RU, LU, RD, LD, UR, UL, DR, DL)
  val boardWithoutPiece = b - pc
  val allReachablePositions = possibleMoves.flatMap(move => 
    eval(pc, move, pc.en, boardWithoutPiece)
  )

  allReachablePositions.toSet
}

def attacked(c: Colour, b: Board): Set[Piece] = {
  val attackingPieces = b.pces.filter(_.col == c)
  val defendingPieces = b.pces.filter(_.col != c)
  
  val reachablePositions = attackingPieces.flatMap(piece => 
    all_moves(piece, b).map(_.pos)
  ).toSet
  
  defendingPieces.filter(piece => 
    reachablePositions.contains(piece.pos)
  ).toSet
}

def attackedN(pc: Piece, b: Board): Int = {
  val enemies = b.pces.filter(_.col != pc.col).toList
  val attackingMoves = enemies.flatMap(piece => all_moves(piece, b))

  attackingMoves.count(_.pos == pc.pos)
}

def protectedN(pc: Piece, b: Board): Int = {
  val allies = (b.pces - pc).filter(_.col == pc.col).toList
  val boardWithoutPiece = b - pc
  val protectingMoves = allies.flatMap(piece => all_moves(piece, boardWithoutPiece))

  protectingMoves.count(_.pos == pc.pos)
}

def legal_moves(pc: Piece, b: Board): Set[Piece] = {
  if (is_king(pc)) {
    all_moves(pc, b).filter(piece => 
      attackedN(piece, Board(b.pces.filter(_.pos != piece.pos))) == 0
    )
  } else {
    all_moves(pc, b)
  }
}
}
