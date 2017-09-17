# ExprCal
A compact library for compiling and solving complex mathematical expressions (still under tests).
To render an expression, Do the following:

String s="1.233---.999.00+sin(30)/cos(60)tan(45)";
int round_up_scale = 4;
double result = new ExpressionRenderer(s, ExpressionRenderer.DEGREE, round_up_scale).render().getResult();

Documentations will be released soon.
