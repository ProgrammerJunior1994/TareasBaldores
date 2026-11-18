; =====================================================
;  PROGRAMA COMPLETO FastDon - Codigo ensamblador
;  Temporales: 0
;  Etiquetas : 8
; =====================================================
; --- declaracion: a = 4 ---
LOAD 4
STORE a
; --- declaracion: b = 6 ---
LOAD 6
STORE b
; --- declaracion: c = 2 ---
LOAD 2
STORE c
; --- leer(x) ---
READ x

; --- t1 = + + id5  ( + ) ---
LOAD +
ADD b
STORE t1

; --- t2 = > > id6  ( > ) ---
LOAD >
GT c
STORE t2

; --- t3 = * * id6  ( * ) ---
LOAD *
MUL c
STORE t3

; --- t4 = - - id6  ( - ) ---
LOAD -
SUB c
STORE t4

; --- t5 = ( < t4  ( < ) ---
LOAD (
LT t4
STORE t5

; --- t6 = t3 && t5  ( && ) ---
LOAD t3
AND t5
STORE t6

; --- t7 = >= >= id18  ( >= ) ---
LOAD >=
GE 10
STORE t7

; --- t8 = id0 || t7  ( || ) ---
LOAD x
OR t7
STORE t8

; --- t1 = * * id6  ( * ) ---
LOAD *
MUL c
STORE t1

; --- t2 = id5 + t1  ( + ) ---
LOAD b
ADD t1
STORE t2

; === si (t8) ===
LOAD t8
JZ L1
L1_SI:
LOAD t2
WRITE
JMP L2
L1:
LOAD "no cumple"
WRITE
L2:
; --- declaracion: i = 1 ---
LOAD 1
STORE i
; --- declaracion: s = 0 ---
LOAD 0
STORE s

; --- t1 = * * id22  ( * ) ---
LOAD *
MUL 2
STORE t1

; --- t2 = <= <= id18  ( <= ) ---
LOAD <=
LE 10
STORE t2

; --- t3 = + + id12  ( + ) ---
LOAD +
ADD i
STORE t3

; --- t4 = < < id23  ( < ) ---
LOAD <
LT 100
STORE t4

; --- t5 = id3 && t4  ( && ) ---
LOAD s
AND t4
STORE t5

; --- t6 = != != id24  ( != ) ---
LOAD !=
NE 6
STORE t6

; --- t7 = id12 || t6  ( || ) ---
LOAD i
OR t6
STORE t7

; --- t1 = * * id27  ( * ) ---
LOAD *
MUL 3
STORE t1

; --- t2 = id12 + t1  ( + ) ---
LOAD i
ADD t1
STORE t2

; --- t3 = - - id28  ( - ) ---
LOAD -
SUB 1
STORE t3

; --- t1 = + + id28  ( + ) ---
LOAD +
ADD 1
STORE t1

; === mientras (t7) ===
L3:
LOAD t7
JZ L4
; --- s = (s + i * 3) - 1 ---
LOAD t3
STORE s
; --- i = i + 1 ---
LOAD t1
STORE i
JMP L3
L4:
; --- leer(a) ---
READ a
; --- leer(b) ---
READ b
; --- leer(c) ---
READ c

; --- t1 = - - id5  ( - ) ---
LOAD -
SUB b
STORE t1

; --- t2 = + + id28  ( + ) ---
LOAD +
ADD 1
STORE t2

; --- t3 = ( > t2  ( > ) ---
LOAD (
GT t2
STORE t3

; --- t4 = * * id22  ( * ) ---
LOAD *
MUL 2
STORE t4

; --- t5 = + + id6  ( + ) ---
LOAD +
ADD c
STORE t5

; --- t6 = ( <= t5  ( <= ) ---
LOAD (
LE t5
STORE t6

; --- t7 = t4 && t6  ( && ) ---
LOAD t4
AND t6
STORE t7

; --- t8 = != != id5  ( != ) ---
LOAD !=
NE b
STORE t8

; --- t9 = id4 || t8  ( || ) ---
LOAD a
OR t8
STORE t9

; --- t1 = + + id5  ( + ) ---
LOAD +
ADD b
STORE t1

; --- t2 = * * id6  ( * ) ---
LOAD *
MUL c
STORE t2

; === si (t9) ===
LOAD t9
JZ L5
L5_SI:
LOAD t2
WRITE
JMP L6
L5:
; --- imprimir(a - b - c) ---
LOAD a
SUB b
SUB c
WRITE
L6:
; --- declaracion: n = 3 ---
LOAD 3
STORE n
; --- declaracion: acc = 1 ---
LOAD 1
STORE acc

; --- t1 = * * id22  ( * ) ---
LOAD *
MUL 2
STORE t1

; --- t2 = >= >= id32  ( >= ) ---
LOAD >=
GE 4
STORE t2

; --- t3 = + + id14  ( + ) ---
LOAD +
ADD n
STORE t3

; --- t4 = < < id33  ( < ) ---
LOAD <
LT 50
STORE t4

; --- t5 = id15 && t4  ( && ) ---
LOAD acc
AND t4
STORE t5

; --- t6 = != != id34  ( != ) ---
LOAD !=
NE 0
STORE t6

; --- t7 = id14 || t6  ( || ) ---
LOAD n
OR t6
STORE t7

; --- t1 = * * id22  ( * ) ---
LOAD *
MUL 2
STORE t1

; --- t2 = - - id28  ( - ) ---
LOAD -
SUB 1
STORE t2

; --- t3 = ( + t2  ( + ) ---
LOAD (
ADD t2
STORE t3

; --- t1 = - - id28  ( - ) ---
LOAD -
SUB 1
STORE t1

; === for (cond = t7) ===
L7:
LOAD t7
JZ L8
; --- acc = (acc * 2) + (n - 1) ---
LOAD t3
STORE acc
; --- n = n - 1 ---
LOAD t1
STORE n
JMP L7
L8:
; --- imprimir(acc) ---
LOAD acc
WRITE
HALT
