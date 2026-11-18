.MODEL SMALL
.STACK 100h
.DATA
    edad dw 0
    calificacion dw 0
    cantidad dw 0
    suma dw 0
    promedio dw 0
    aprobados dw 0
    reprobados dw 0
    opcion dw 0
    contador dw 0
    numero dw 0
    pares dw 0
    impares dw 0
    inscrito dw 0
    asistencia dw 0
    acceso dw 0
    nombre db 255,0,256 dup('$')
    escuela db 255,0,256 dup('$')
    escuela_texto db ' TSJ ','$'
    carrera db 255,0,256 dup('$')
    carrera_texto db ' Sistemas ','$'
    grupo db 255,0,256 dup('$')
    salida8 db ' Alumno autorizado ','$'
    salida9 db ' Alumno no autorizado ','$'
    i dw 0
    salida23 db ' Excelente calificacion ','$'
    salida28 db ' Alumno aprobado ','$'
    salida29 db ' Alumno reprobado ','$'
    salida32 db ' Consultar promedio ','$'
    salida34 db ' Consultar aprobados ','$'
    salida36 db ' Consultar reprobados ','$'
    salida37 db ' Opcion no valida ','$'
    j dw 0
    salida51 db ' Nombre del alumno: ','$'
    salida52 db ' Escuela: ','$'
    salida53 db ' Carrera: ','$'
    salida54 db ' Edad: ','$'
    salida55 db ' Promedio: ','$'
    salida56 db ' Total de aprobados: ','$'
    salida57 db ' Total de reprobados: ','$'
    salida58 db ' Numeros pares: ','$'
    salida59 db ' Numeros impares: ','$'
    numero dw 0
    digitos db 6 dup('$')
.CODE
main PROC
    mov ax, @data
    mov ds, ax
    mov es, ax
    mov ax, 0
    mov suma, ax
    mov ax, 0
    mov aprobados, ax
    mov ax, 0
    mov reprobados, ax
    mov ax, 0
    mov contador, ax
    mov ax, 0
    mov pares, ax
    mov ax, 0
    mov impares, ax
    mov ax, 1
    mov inscrito, ax
    mov ax, 0
    mov acceso, ax
    lea dx, escuela_texto
    lea di, escuela+2
    mov si, dx
    call copiar_cadena
    lea dx, carrera_texto
    lea di, carrera+2
    mov si, dx
    call copiar_cadena
    lea dx, nombre
    mov ah, 0Ah
    int 21h
    call terminar_cadena
    call leer_entero
    mov edad, ax
    call leer_entero
    mov asistencia, ax
    call leer_entero
    mov cantidad, ax
    mov ax, edad
    push ax
    mov ax, 18
    mov bx, ax
    pop ax
    cmp ax, bx
    jge verdadero0
    mov ax, 0
    jmp comparacion_fin1
verdadero0:
    mov ax, 1
comparacion_fin1:
    push ax
    mov ax, asistencia
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    cmp ax, bx
    je verdadero2
    mov ax, 0
    jmp comparacion_fin3
verdadero2:
    mov ax, 1
comparacion_fin3:
    mov bx, ax
    pop ax
    and ax, bx
    cmp ax, 0
    jne bool_v4
    mov ax, 0
    jmp bool_f5
bool_v4:
    mov ax, 1
bool_f5:
    cmp ax, 0
    je sino6
    mov ax, 1
    mov acceso, ax
    lea dx, salida8
    mov ah, 09h
    int 21h
    jmp fin_si7
sino6:
    lea dx, salida9
    mov ah, 09h
    int 21h
fin_si7:
    mov ax, 0
    mov i, ax
para10:
    mov ax, i
    push ax
    mov ax, cantidad
    mov bx, ax
    pop ax
    cmp ax, bx
    jl verdadero12
    mov ax, 0
    jmp comparacion_fin13
verdadero12:
    mov ax, 1
comparacion_fin13:
    cmp ax, 0
    je fin_para11
    call leer_entero
    mov calificacion, ax
    mov ax, suma
    push ax
    mov ax, calificacion
    mov bx, ax
    pop ax
    add ax, bx
    mov suma, ax
    mov ax, calificacion
    push ax
    mov ax, 70
    mov bx, ax
    pop ax
    cmp ax, bx
    jge verdadero15
    mov ax, 0
    jmp comparacion_fin16
verdadero15:
    mov ax, 1
comparacion_fin16:
    cmp ax, 0
    je sino17
    mov ax, aprobados
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    add ax, bx
    mov aprobados, ax
    jmp fin_si18
sino17:
    mov ax, reprobados
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    add ax, bx
    mov reprobados, ax
fin_si18:
    mov ax, calificacion
    push ax
    mov ax, 90
    mov bx, ax
    pop ax
    cmp ax, bx
    jge verdadero19
    mov ax, 0
    jmp comparacion_fin20
verdadero19:
    mov ax, 1
comparacion_fin20:
    cmp ax, 0
    je sino21
    lea dx, salida23
    mov ah, 09h
    int 21h
    jmp fin_si22
sino21:
    mov ax, calificacion
    push ax
    mov ax, 70
    mov bx, ax
    pop ax
    cmp ax, bx
    jge verdadero24
    mov ax, 0
    jmp comparacion_fin25
verdadero24:
    mov ax, 1
comparacion_fin25:
    cmp ax, 0
    je sino26
    lea dx, salida28
    mov ah, 09h
    int 21h
    jmp fin_si27
sino26:
    lea dx, salida29
    mov ah, 09h
    int 21h
fin_si27:
fin_si22:
incremento14:
    inc i
    jmp para10
fin_para11:
    mov ax, suma
    push ax
    mov ax, cantidad
    mov bx, ax
    pop ax
    cwd
    idiv bx
    mov promedio, ax
    call leer_entero
    mov opcion, ax
    mov ax, opcion
    cmp ax, 1
    jne case_siguiente31
    lea dx, salida32
    mov ah, 09h
    int 21h
    mov ax, promedio
    call imprimir_entero
    jmp fin_switch30
    jmp fin_switch30
case_siguiente31:
    cmp ax, 2
    jne case_siguiente33
    lea dx, salida34
    mov ah, 09h
    int 21h
    mov ax, aprobados
    call imprimir_entero
    jmp fin_switch30
    jmp fin_switch30
case_siguiente33:
    cmp ax, 3
    jne case_siguiente35
    lea dx, salida36
    mov ah, 09h
    int 21h
    mov ax, reprobados
    call imprimir_entero
    jmp fin_switch30
    jmp fin_switch30
case_siguiente35:
    lea dx, salida37
    mov ah, 09h
    int 21h
    jmp fin_switch30
fin_switch30:
    call leer_entero
    mov numero, ax
mientras38:
    mov ax, numero
    push ax
    mov ax, 0
    mov bx, ax
    pop ax
    cmp ax, bx
    jne verdadero40
    mov ax, 0
    jmp comparacion_fin41
verdadero40:
    mov ax, 1
comparacion_fin41:
    cmp ax, 0
    je fin_mientras39
    mov ax, numero
    push ax
    mov ax, 2
    mov bx, ax
    pop ax
    cwd
    idiv bx
    mov ax, dx
    push ax
    mov ax, 0
    mov bx, ax
    pop ax
    cmp ax, bx
    je verdadero42
    mov ax, 0
    jmp comparacion_fin43
verdadero42:
    mov ax, 1
comparacion_fin43:
    cmp ax, 0
    je sino44
    mov ax, pares
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    add ax, bx
    mov pares, ax
    jmp fin_si45
sino44:
    mov ax, impares
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    add ax, bx
    mov impares, ax
fin_si45:
    call leer_entero
    mov numero, ax
    jmp mientras38
fin_mientras39:
    mov ax, 0
    mov j, ax
para46:
    mov ax, j
    push ax
    mov ax, 5
    mov bx, ax
    pop ax
    cmp ax, bx
    jl verdadero48
    mov ax, 0
    jmp comparacion_fin49
verdadero48:
    mov ax, 1
comparacion_fin49:
    cmp ax, 0
    je fin_para47
    mov ax, contador
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    add ax, bx
    mov contador, ax
    mov ax, contador
    call imprimir_entero
incremento50:
    inc j
    jmp para46
fin_para47:
    lea dx, salida51
    mov ah, 09h
    int 21h
    lea dx, nombre+2
    call imprimir_cadena
    lea dx, salida52
    mov ah, 09h
    int 21h
    lea dx, escuela+2
    call imprimir_cadena
    lea dx, salida53
    mov ah, 09h
    int 21h
    lea dx, carrera+2
    call imprimir_cadena
    lea dx, salida54
    mov ah, 09h
    int 21h
    mov ax, edad
    call imprimir_entero
    lea dx, salida55
    mov ah, 09h
    int 21h
    mov ax, promedio
    call imprimir_entero
    lea dx, salida56
    mov ah, 09h
    int 21h
    mov ax, aprobados
    call imprimir_entero
    lea dx, salida57
    mov ah, 09h
    int 21h
    mov ax, reprobados
    call imprimir_entero
    lea dx, salida58
    mov ah, 09h
    int 21h
    mov ax, pares
    call imprimir_entero
    lea dx, salida59
    mov ah, 09h
    int 21h
    mov ax, impares
    call imprimir_entero
fin_programa:
    mov ax, 4C00h
    int 21h
main ENDP

imprimir_entero PROC
    push ax
    push bx
    push cx
    push dx
    mov bx, 10
    xor cx, cx
    cmp ax, 0
    jge conv_pos
    push ax
    mov dl, '-'
    mov ah, 02h
    int 21h
    pop ax
    neg ax
conv_pos:
    xor dx, dx
conv_loop:
    div bx
    push dx
    inc cx
    xor dx, dx
    cmp ax, 0
    jne conv_loop
conv_print:
    pop dx
    add dl, '0'
    mov ah, 02h
    int 21h
    loop conv_print
    pop dx
    pop cx
    pop bx
    pop ax
    ret
imprimir_entero ENDP
leer_entero PROC
    push bx
    push cx
    push dx
    xor bx, bx
leer_digito:
    mov ah, 01h
    int 21h
    cmp al, 13
    je leer_fin
    cmp al, '0'
    jb leer_digito
    cmp al, '9'
    ja leer_digito
    sub al, '0'
    xor ah, ah
    mov cx, ax
    mov ax, bx
    mov dx, 10
    mul dx
    add ax, cx
    mov bx, ax
    jmp leer_digito
leer_fin:
    mov ax, bx
    pop dx
    pop cx
    pop bx
    ret
leer_entero ENDP
imprimir_cadena PROC
    push ax
    push dx
    mov ah, 09h
    int 21h
    pop dx
    pop ax
    ret
imprimir_cadena ENDP
copiar_cadena PROC
    push ax
    push si
    push di
copiar_loop:
    lodsb
    cmp al, '$'
    je copiar_fin
    stosb
    jmp copiar_loop
copiar_fin:
    mov al, '$'
    stosb
    pop di
    pop si
    pop ax
    ret
copiar_cadena ENDP
terminar_cadena PROC
    push ax
    push bx
    push di
    push si
    mov si, dx
    xor bx, bx
    mov bl, [si+1]
    lea di, [si+2]
    add di, bx
    mov byte ptr [di], '$'
    pop si
    pop di
    pop bx
    pop ax
    ret
terminar_cadena ENDP

END main
