.MODEL SMALL
.STACK 100h
.DATA
    producto dw 0
    cantidad dw 0
    vendidos dw 0
    opcion dw 0
    precio dw 0
    total dw 0
    nombre db 255,0,256 dup('$')
    tienda db 255,0,256 dup('$')
    tienda_texto db ' Mi tienda ','$'
    disponible dw 0
    salida8 db ' Producto disponible ','$'
    salida9 db ' Producto no disponible ','$'
    i dw 0
    salida18 db ' Opcion incorrecta ','$'
    salida23 db ' Producto: ','$'
    salida24 db ' Vendidos: ','$'
    salida25 db ' Total: ','$'
    salida26 db ' Inventario: ','$'
    numero dw 0
    digitos db 6 dup('$')
.CODE
main PROC
    mov ax, @data
    mov ds, ax
    mov es, ax
    mov ax, 10
    mov cantidad, ax
    mov ax, 0
    mov vendidos, ax
    mov ax, 50
    mov precio, ax
    mov ax, 0
    mov total, ax
    lea dx, tienda_texto
    lea di, tienda+2
    mov si, dx
    call copiar_cadena
    mov ax, 1
    mov disponible, ax
    lea dx, nombre
    mov ah, 0Ah
    int 21h
    call terminar_cadena
    call leer_entero
    mov producto, ax
    mov ax, producto
    push ax
    mov ax, 0
    mov bx, ax
    pop ax
    cmp ax, bx
    jg verdadero0
    mov ax, 0
    jmp comparacion_fin1
verdadero0:
    mov ax, 1
comparacion_fin1:
    push ax
    mov ax, disponible
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
    mov ax, producto
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
    mov ax, vendidos
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    add ax, bx
    mov vendidos, ax
    mov ax, total
    push ax
    mov ax, precio
    mov bx, ax
    pop ax
    add ax, bx
    mov total, ax
incremento14:
    inc i
    jmp para10
fin_para11:
    call leer_entero
    mov opcion, ax
    mov ax, opcion
    cmp ax, 1
    jne case_siguiente16
    mov ax, cantidad
    push ax
    mov ax, vendidos
    mov bx, ax
    pop ax
    sub ax, bx
    mov cantidad, ax
    jmp fin_switch15
    jmp fin_switch15
case_siguiente16:
    cmp ax, 2
    jne case_siguiente17
    mov ax, cantidad
    push ax
    mov ax, vendidos
    mov bx, ax
    pop ax
    add ax, bx
    mov cantidad, ax
    jmp fin_switch15
    jmp fin_switch15
case_siguiente17:
    lea dx, salida18
    mov ah, 09h
    int 21h
    jmp fin_switch15
fin_switch15:
mientras19:
    mov ax, cantidad
    push ax
    mov ax, 0
    mov bx, ax
    pop ax
    cmp ax, bx
    jg verdadero21
    mov ax, 0
    jmp comparacion_fin22
verdadero21:
    mov ax, 1
comparacion_fin22:
    cmp ax, 0
    je fin_mientras20
    mov ax, cantidad
    push ax
    mov ax, 1
    mov bx, ax
    pop ax
    sub ax, bx
    mov cantidad, ax
    jmp mientras19
fin_mientras20:
    lea dx, salida23
    mov ah, 09h
    int 21h
    lea dx, nombre+2
    call imprimir_cadena
    lea dx, salida24
    mov ah, 09h
    int 21h
    mov ax, vendidos
    call imprimir_entero
    lea dx, salida25
    mov ah, 09h
    int 21h
    mov ax, total
    call imprimir_entero
    lea dx, salida26
    mov ah, 09h
    int 21h
    mov ax, cantidad
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
