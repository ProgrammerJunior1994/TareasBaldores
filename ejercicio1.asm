.MODEL SMALL
.STACK 100h
.DATA
    edad dw 0
    i dw 0
    salida9 db 'Tienes 30 años','$'
    numero dw 0
    digitos db 6 dup('$')
.CODE
main PROC
    mov ax, @data
    mov ds, ax
    mov es, ax
    mov ax, 20
    mov edad, ax
    mov ax, 0
    mov i, ax
para0:
    mov ax, i
    push ax
    mov ax, 30
    mov bx, ax
    pop ax
    cmp ax, bx
    jle verdadero2
    mov ax, 0
    jmp comparacion_fin3
verdadero2:
    mov ax, 1
comparacion_fin3:
    cmp ax, 0
    je fin_para1
    inc edad
    mov ax, edad
    push ax
    mov ax, 30
    mov bx, ax
    pop ax
    cmp ax, bx
    je verdadero5
    mov ax, 0
    jmp comparacion_fin6
verdadero5:
    mov ax, 1
comparacion_fin6:
    cmp ax, 0
    je sino7
    lea dx, salida9
    mov ah, 09h
    int 21h
sino7:
incremento4:
    inc i
    jmp para0
fin_para1:
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
