; Codigo ensamblador generado desde triplos - FastDon
; Temporales: 3

; --- t1 = * * id17  ( * ) ---
LOAD *
MUL 3
STORE t1

; --- t2 = id6 + t1  ( + ) ---
LOAD c
ADD t1
STORE t2

; --- t3 = + + id18  ( + ) ---
LOAD +
ADD 2
STORE t3
HALT
