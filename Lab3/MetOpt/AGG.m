function x = AGG(p, q, alpha, beta, N, b)
syms x1 x2 x3 x4 x5;
z = (-2*x1+x2+2)/(x1+3*x2+4);
B = eye(3);
b = [4 6 14]';
A = [N, B];
sizeA = size(A);
m = sizeA(1,1);
Indexes = zeros(1, m);
Indexes = [3, 4, 5];
n = sizeA(1,2);
x = A\b

y = [diff(z, 'x1'), diff(z, 'x2'), diff(z, 'x3'), diff(z, 'x4'), diff(z, 'x5')];
x = [0, 0, 4, 6, 14]';
Indexes = [3, 4, 5]';

while 1 == 1
    nabla = subs(y, {x1, x2, x3, x4, x5},{x(1), x(2), x(3), x(4), x(5)})
    nablaN = [];
    nablaB = [];
    N = [];
    B = [];
    for i = 1:1:n
        flag = 0;
        for j = 1:1:m
            if i == Indexes(j)
                flag = 1;
            end
        end
        if flag == 0
            nablaN = [nablaN, nabla(i)];
            N = [N, A(:, i)];
        end
    end
    for i = 1:1:m
        nablaB = [nablaB, nabla(Indexes(i))];
        B = [B, A(:, Indexes(i))];
    end
    rN = zeros(10, 1);
    rN = nablaN-(nablaB)*N
    if rN >= 0
        break;
    end
    maxInd = 0;
    minInd = 0;
    minInd2 = 0;
    for i = 1:1:n-m
        if (maxInd == 0 && rN(i) <= 0) || (rN(i) <= 0 && rN(maxInd) > rN(i))
            maxInd = i;
        end
    end
    bc = b\B;
    yc = A(:, maxInd) \ B;
    for i = 1: 1: m
        if yc(i) > 0
            if minInd == 0 || bc(i)/yc(i) < bc(minInd)/yc(minInd)
                minInd = Indexes(i);
                minInd2 = i;
            end
        end
    end
    r = minInd
    j = maxInd;
%     yt = yc(r);
    i = 1;
   
    while(Indexes(i) ~= r)
      i = i + 1;
    end
    Indexes(i) = j;

    t = A(:, minInd2);

    newA(minInd2, :) = A(minInd2, :)/A(minInd2, j);
    x(n) = x(n)/A(minInd2,j);
    
    ksi = x(n);
    
    for i = 1:1:m
        for k = 1:1:n
            if i ~= minInd2 && k ~=j
             newA(i, k) = A(i, k) - A(minInd2, k)*A(i, j)/A(minInd2, j);
            end
        end
    end
    newA
    for i = 1:1:m
        if i ~= r
            x(Indexes(i)) = x(Indexes(i))-A(i, j)*ksi;
        end; 
    end
     t = x(j);
    x(j)= x(r);
    x(r) = t;
A = newA

end
end