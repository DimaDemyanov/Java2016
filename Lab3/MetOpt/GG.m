%Gilmor Gomory
syms x1 x2;
z = (-2*x1+x2+2)/(x1+3*x2+4);
p = [-2; 1];
q = [1 ; 3];
alpha = 2;
beta = 4;
N = [-1 1;
     0 1;
     2 1];
 b = [4 6 14]';
 %graphic
 x1 = 0:0.01:10;
 
 ylim([0, 10])
 x2 = (b(1) - N(1,1) * x1) / N(2,2);
 plot(x1, x2, 'LineWidth',2)
 hold all
 grid on
 
 ylim([0, 10])
 x2 = (b(2) - N(2,1)*x1) / N(2,2)
 plot(x1, x2, 'LineWidth',2)
 
 ylim([0, 10])
 x2 = (b(3) - N(3,1) * x1) / N(3,2);
 plot(x1, x2, 'LineWidth',2)
 
 xlabel('x1')
 ylabel('x2')
 legend('1', '2', '3')
%solve
x = AGG(p, q, alpha, beta, N, b)
x(1)
x(2)
subs(z, {x1, x2}, {x(1), x(2)})