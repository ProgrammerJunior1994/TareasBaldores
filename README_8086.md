# Generacion de ensamblador 8086 para FastDon

Se agrego `Compilador8086.java` para traducir programas FastDon a ensamblador de 16 bits compatible con el estilo de sintaxis de EMU8086.

## Como ejecutar desde NetBeans

1. Abre el proyecto `ExpresionesFastDon`.
2. Ejecuta el proyecto.
3. En el menu principal selecciona la opcion `6`.
4. Escribe la ruta completa del archivo `.txt` que contiene el programa FastDon.
5. Se genera `codigo_ensamblador.asm` en el directorio de ejecucion del proyecto.
6. Abre ese archivo en EMU8086 y ensambla/ejecuta.

## Ejecutar directamente desde consola

Desde la carpeta `ExpresionesFastDon`:

```bash
javac -encoding windows-1252 -d build/classes src/AnalizadorLexico/*.java src/expresionesfastdon/ExpresionesFastDon.java
java -cp build/classes AnalizadorLexico.Compilador8086 ../ejemplos/ejercicio1.txt codigo_ensamblador.asm
```

## Elementos implementados

- Tipos `int`, `boolean` y `String`.
- Declaracion e inicializacion de variables.
- Asignaciones y operadores `++` y `--`.
- Operaciones `+`, `-`, `*`, `/`, `%` y `**`.
- Comparaciones `==`, `!=`, `>`, `<`, `>=` y `<=`.
- Operadores logicos `&&`, `||` y `!`.
- Estructuras `if`, `else`, `while`, `for` y `switch` con `case`, `default` y `break`.
- Salida con `print` para literales, variables numericas y cadenas.
- Entrada con `inputI`, `inputB` e `inputS`.
- Generacion de instrucciones 8086 como `MOV`, `ADD`, `SUB`, `IMUL`, `IDIV`, `CMP`, saltos condicionales e interrupciones DOS `INT 21h`.

## Consideraciones

- Las variables enteras y booleanas se representan con palabras de 16 bits. Los booleanos usan `0` y `1`.
- La entrada numerica admite numeros enteros positivos. La salida numerica muestra el signo si el valor es negativo.
- Las cadenas se manejan como texto terminado en `$`, usando servicios DOS.
- La concatenacion admitida en `print` es mediante `++` entre literales de texto y variables `String` o valores numericos.
- El operador `**` se genera mediante multiplicaciones repetidas y admite exponentes enteros no negativos.
- Se trabaja con variables declaradas antes de su uso. El analizador mantiene un unico espacio de nombres para todo el programa.
- El analizador LR de expresiones y su `pilaEm` original se conservaron. La opcion 6 usa el nuevo compilador de programas completos; no elimina ni sustituye esa pila.

Los archivos de `ejemplos` son programas de prueba que venian en el repositorio.


## Generar los cuatro ejercicios automáticamente

Los archivos `ejercicio1.txt`, `ejercicio2.txt`, `ejercicio3.txt` y `ejercicio4.txt` deben permanecer en la carpeta raíz `TareasBaldores`, al mismo nivel que la carpeta `ExpresionesFastDon`.

Al ejecutar el programa y seleccionar la opción 6, ya no se solicitan rutas ni nombres. El programa localiza la carpeta raíz, lee los cuatro archivos y genera automáticamente `ejercicio1.asm`, `ejercicio2.asm`, `ejercicio3.asm` y `ejercicio4.asm` en esa misma carpeta.
