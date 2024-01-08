from scipy import optimize

def main():
    lines = [
        [19, 13, 30, -2, 1, -2],
        [18, 19, 22, -1, -1, -2],
        [12, 31, 28, -1, -2, -1],
        # [212542581053874, 357959731032403, 176793474286781, -88, -256, -240],
        # [154677220587564, 207254130208265, 139183938188421, 184, 74, 235],
        # [216869547613134, 38208083662943, 397740686492049, 109, 262, -66],
    ]

    def func(x):
        s = []
        print(x)
        for i, l in enumerate(lines):
            for j in range(3):
                # print('%.2f + %.2f * %.2f - (%.2f + %.2f * %.2f)' % (l[i], x[i], l[i + 3], x[i + 3], x[i], x[i + 6]))
                s.append(l[j] + x[i] * l[j + 3] - (x[j + 3] + x[i] * x[j + 6]))
        # print(s)

        return s

    def func2(x):
        s = []
        print(x)
        l0 = lines[2]
        for i, l in enumerate(lines[0:2]):
            for j in range(3):
                # print('%.2f + %.2f * %.2f - (%.2f + %.2f * %.2f)' % (l[i], x[i], l[i + 3], x[i + 3], x[i], x[i + 6]))
                s.append(
                    (l[j] - l0[j]) + x[i] * (l[j + 3] - l0[j + 3]) - ((x[j + 2] - l0[j]) + x[i] * (x[j + 4] - l0[j])))
                # print(s)
        return s

    root = optimize.fsolve(func2, [1, 1, 1, 1, 1, 1])
    # root = optimize.fsolve(func, [1000,1000,1000,1000,1000,1000,1000,1000,1000,])
    print(root)


if __name__ == '__main__':
    main()
