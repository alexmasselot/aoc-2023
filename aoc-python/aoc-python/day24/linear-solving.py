import numpy as np


def main():
    lines = [
        [212542581053874, 357959731032403, 176793474286781, -88, -256, -240],
        [154677220587564, 207254130208265, 139183938188421, 184, 74, 235],
        [216869547613134, 38208083662943, 397740686492049, 109, 262, -66],
    ]
    v = [139, -93, 245]
    m = np.array([[1, 0, v[0] - lines[0][3], 0],
                  [1, 0, 0, v[0] - lines[1][3]],
                  [0, 1, v[1] - lines[0][4], 0],
                  [0, 1, 0, v[1] - lines[1][4]]])

    y = np.array([lines[0][0], lines[1][0], lines[0][1], lines[1][1]])

    # Calculate the inverse of M
    m_inv = np.linalg.inv(m)
    x = np.dot(m_inv, y)
    print(x)
    print(y)
    print(np.dot(m, m_inv))
    (p_x, p_y, t_0, t_1) = x
    print('p_x=%.5f p_y=%.5f t_0=%.5f t_1=%.5f ' % (p_x, p_y, t_0, t_1))
    print('--------------------')
    m = np.array([[1, 0, v[0] - lines[0][3], 0],
                  [1, 0, 0, v[0] - lines[1][3]],
                  [0, 1, v[2] - lines[0][5], 0],
                  [0, 1, 0, v[2] - lines[1][5]]])

    y = np.array([lines[0][0], lines[1][0], lines[0][2], lines[1][2]])

    # Calculate the inverse of M
    m_inv = np.linalg.inv(m)
    x = np.dot(m_inv, y)
    print(x)
    print(y)
    p_z=x[1]
    print('%.4f'%p_z)
    print('--------------------')
    #p_z = lines[0][2] + round(t_1) * (lines[0][5] - v[2])
    print('pos=%.5f, %.5f, %.5f' % (p_x, p_y, p_z))
    print('pos=%d, %d, %d' % (round(p_x), round(p_y), round(p_z)))
    print('vel=%d, %d, %d' % (v[0], v[1], v[2]))
    print("sum p: %d" % (p_x + p_y + p_z))
    print('X L: %d S: %d' % (lines[0][0] + t_0 * lines[0][3], p_x + t_0 * v[0]))
    print('X L: %d S: %d' % (lines[1][0] + t_1 * lines[1][3], p_x + t_1 * v[0]))
    print('Y L: %d S: %d' % (lines[0][1] + t_0 * lines[0][4], p_y + t_0 * v[1]))
    print('Y L: %d S: %d' % (lines[1][1] + t_1 * lines[1][4], p_y + t_1 * v[1]))
    print('Z L: %d S: %d' % (lines[0][2] + t_0 * lines[0][5], p_z + t_0 * v[2]))
    print('Z L: %d S: %d' % (lines[1][2] + t_1 * lines[1][5], p_z + t_1 * v[2]))
    # 714588353765228 is too high
    # 317477159965768 is too low
    # 615813068442680 is too low


if __name__ == '__main__':
    main()
