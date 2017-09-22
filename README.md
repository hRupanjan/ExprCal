# ExprCal
A compact library for compiling and solving complex mathematical expressions (still under tests).
To render an expression, Do the following:

String s="1.233---.999.00+sin(pow(2,PI))/cos(60)tan(45)";
int round_up_scale = 4;
double result = new ExpressionRenderer(s, ExpressionRenderer.DEGREE, round_up_scale).render().getResult();

Features:-
Constants follow Uppercase paradigm. So always declare Constants in UpperCase to avoid error.
Eg:- Constant.add("X",Constant.get("PI")); Constant.add("Y",3.0);

Functions follow Lowercase paradigm. So always declare Functions in Lowercase to avoid error.
Eg:- Function.add("si", Math.class.getDeclaredMethod("sin", double.class));

New Feature:-
New method chaining for some functions.
ExpressionRenderer e = new ExpressionRenderer(ExpressionRenderer.DEGREE, 4);
double res = e.add("X=9","X=-sqrt(X)","Y=4").add("abs", Math.class.getDeclaredMethod("abs", double.class))
                .setExpression(s).render().getResult().getNumber();

Warning:
Functions and Constants must be declared before setting the expression.
Documentations will be released soon.
