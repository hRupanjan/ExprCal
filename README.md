# ExprCal
A compact library for compiling and solving complex mathematical expressions (still under tests).
To render an expression, Do the following:

String s="1.233---.999.00+sin(pow(2,PI))/cos(60)tan(45)";
int round_up_scale = 4;
double result = new ExpressionRenderer(s, ExpressionRenderer.DEGREE, round_up_scale).render().getResult();

Constants follow Uppercase paradigm. So always declare Constants in UpperCase to avoid error.
Eg:- Constant.add("X",Constant.get("PI")); Constant.add("Y",3.0);

New Feature:-
Functions follow Lowercase paradigm. So always declare Functions in Lowercase to avoid error.
Eg:- Function.add("si", Math.class.getDeclaredMethod("sin", double.class));

Documentations will be released soon.
