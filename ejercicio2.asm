.MODEL SMALL
.STACK 100h
.DATA
    jaime dw 0
    amelie dw 0
    salida8 db 'Son felices por siempre','$'
    salida17 db 'Separados para siempre','$'
    numero dw 0
    digitos db 6 dup('$')
.CODE
main PROC
    mov ax, @data
    mov ds, ax
    mov es, ax
    mov ax, 1
    mov jaime, ax
    mov ax, 0
    mov amelie, ax
    mov ax, jaime
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    cmp ax, bx
    je verdadero0
    mov ax, 0
    jmp comparacion_fin1
verdadero0:
    mov ax, 1
comparacion_fin1:
    push ax
    mov ax, amelie
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
    lea dx, salida8
    mov ah, 09h
    int 21h
sino6:
    mov ax, jaime
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    cmp ax, bx
    je verdadero9
    mov ax, 0
    jmp comparacion_fin10
verdadero9:
    mov ax, 1
comparacion_fin10:
    push ax
    mov ax, amelie
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    cmp ax, bx
    jne verdadero11
    mov ax, 0
    jmp comparacion_fin12
verdadero11:
    mov ax, 1
comparacion_fin12:
    mov bx, ax
    pop ax
    and ax, bx
    cmp ax, 0
    jne bool_v13
    mov ax, 0
    jmp bool_f14
bool_v13:
    mov ax, 1
bool_f14:
    cmp ax, 0
    je sino15
    lea dx, salida17
    mov ah, 09h
    int 21h
sino15:
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
