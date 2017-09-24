# ExprCal
A compact library for compiling and solving complex mathematical expressions (still under tests).
A `String` expression is brought into Object Model for better processing & greater readability.

## Getting Started
Basic setup to get result of an expression
### Initial Setup
* [Install Maven] (https://www.tutorialspoint.com/maven/maven_environment_setup.htm)
* Clone or Download the [repository] (https://github.com/hRupanjan/ExprCal).
* Go to the project folder from your native terminal.
* Build and Make an usable .jar file using command `mvn assembly:single`
* Get the jar from `.../target` folder.

### Render an expression
```
String s="1.233---.999.00+sin(pow(2,PI))/cos(60)tan(45)";
int round_up_scale = 4;
double result = new ExpressionRenderer(s, ExpressionRenderer.DEGREE, round_up_scale).render().getResult();
```

## Features
Every `Expression` type object has `ExpressionFramgment` objects in it stored into a hash-map
Types of `ExpressionFramgment`s:-
    * Number
    * Function
    * Expression
    * Constant
    * Operator
    * Sign
    * Bracket

### Expression types that are accepted 
    * [(Number*)(Function*)(Constant*)] - [(Number*)[//*](Function*)[//*](Constant*)] (represented in `regex`)
    * [(Number+|Function+|Constant+)Operator(Number+|Function+|Constant+)] (represented in `regex`)
    * Number String [1.233.999.00.33] - 1.233 * 0.999 * 0.00 * 0.33
    * Function inside Function i.e. `f(g(X))` is accepted.
    * Fragments with spaces can also be written for better expression readability

### Constant Pool:
Constants follow Uppercase paradigm. So always declare Constants in UpperCase to avoid error.
Types of Constant declarations:-
    * "X" means X=0
    * "X=" means X=0
    * "X=10" means X=10.0
    * "X=0.0" means X=0.0
    * "X=3*4+sqrt(9)" means X=15.0
    * "X=A" means X=A=some pre-declared value
```
Constant.add("X",Constant.get("PI")); Constant.add("Y",3.0);
Constant.add("U","X=U","X=-sqrt(X)","Y=4");
```
or
```
ExpressionRenderer e = new ExpressionRenderer(ExpressionRenderer.DEGREE, 4);
e.add("U","X=U","X=-sqrt(X)","Y=4");
```

### Function Pool:
`static` methods of any `class` can be inserted in the Function Pool.
The first parameter should be the name with which it will be known in the pool & second should be the method itself.
But Functions follow some rules:-
    * Functions follow Lowercase paradigm. So always declare Functions in Lowercase to avoid error.
    * The parameters passed should be Numbers
    * The return type of the Functions should be Number
```
Function.add("hlsin", Math.class.getDeclaredMethod("sin", double.class));
```
or
```
ExpressionRenderer e = new ExpressionRenderer(ExpressionRenderer.DEGREE, 4);
e.add("abs", Math.class.getDeclaredMethod("abs", double.class));
```

### Method Chaining:
Method chaining has been introduced for some functions invoked via an 'ExpressionRenderer' object.
```
ExpressionRenderer e = new ExpressionRenderer(ExpressionRenderer.DEGREE, 4);
double res = e.add("U","X=U","X=-sqrt(X)","Y=4").add("abs", Math.class.getDeclaredMethod("abs", double.class)).setExpression(s).render().getResult();
```
###Warning:
* `Function`s and `Constant`s must be declared before setting the expression.
* Don't declare Constants together. viz:- `don't write "PIX"` `write "PI*X"`
