
const tokens = {
  admin: {
    token: 'admin-token'
  },
  editor: {
    token: 'editor-token'
  }
}

const users = {
  'admin-token': {
    roles: ['admin'],
    introduction: 'I am a super administrator',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Super Admin'
  },
  'editor-token': {
    roles: ['editor'],
    introduction: 'I am an editor',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Normal Editor'
  }
}

module.exports = [
  // user login
  {
    url: '/vue-admin-template/user/login',
    type: 'post',
    response: config => {
      const { username } = config.body
      const token = tokens[username]

      // mock error
      if (!token) {
        return {
          code: 60204,
          message: 'Account and password are incorrect.'
        }
      }

      return {
        code: 20000,
        data: token
      }
    }
  },

  // get user info
  {
    url: '/vue-admin-template/user/info\.*',
    type: 'get',
    response: config => {
      const { token } = config.query
      const info = users[token]

      // mock error
      if (!info) {
        return {
          code: 50008,
          message: 'Login failed, unable to get user details.'
        }
      }

      return {
        code: 20000,
        data: info
      }
    }
  },

  // user logout
  {
    url: '/vue-admin-template/user/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  },

  // user list
  {
    url: '/vue-admin-template/user/list',
    type: 'get',
    response: config => {
      const { page = 1, pageSize = 10, username, phone } = config.query;
      const mockList = [];
      for (let i = 0; i < 20; i++) {
        const surnames = ['张', '李', '王', '陈', '刘', '杨', '赵', '黄', '周', '吴'];
        const firstNames = ['伟', '芳', '娜', '敏', '静', '强', '磊', '军', '勇', '艳'];
        const domains = ['example.com', 'gmail.com', '163.com', 'qq.com'];
        const id = i + 1;
        const surname = surnames[Math.floor(Math.random() * surnames.length)];
        const firstName = firstNames[Math.floor(Math.random() * firstNames.length)];
        const name = `${surname}${firstName}`;
        const phoneNum = `1${String(Math.random()).slice(2, 12)}`;
        const email = `${name}@${domains[Math.floor(Math.random() * domains.length)]}`;
        const role = i % 5 === 0 ? '管理员' : '普通用户';
        const status = Math.random() > 0.5 ? '在线' : '离线';
        mockList.push({ id, username: name, phone: phoneNum, email, role, status });
      }
      const filteredList = mockList.filter(item => {
        return (!username || item.username.includes(username)) &&
               (!phone || item.phone.includes(phone));
      });
      const total = filteredList.length;
      const start = (page - 1) * pageSize;
      const end = start + pageSize;
      return {
        code: 20000,
        data: {
          list: filteredList.slice(start, end),
          total: total
        }
      };
    }
  },
  // 新增用户
  {
    url: '/api/user/add',
    type: 'post',
    response: config => {
      const { username, phone, email, role, status } = config.body
      userList.push({
        id: Mock.mock('@id'),
        username,
        phone,
        email,
        role,
        status
      })
      return {
        code: 200,
        data: {}
      }
    }
  },
  // 编辑用户
  {
    url: '/api/user/update',
    type: 'post',
    response: config => {
      const { id, username, phone, email, role, status } = config.body
      const index = userList.findIndex(item => item.id === id)
      if (index === -1) {
        return {
          code: 500,
          message: '用户不存在'
        }
      }
      userList[index] = { ...userList[index], username, phone, email, role, status }
      return {
        code: 200,
        data: {}
      }
    }
  },
  // 删除用户
  {
    url: '/api/user/delete',
    type: 'post',
    response: config => {
      const { id } = config.body
      const index = userList.findIndex(item => item.id === id)
      if (index === -1) {
        return {
          code: 500,
          message: '用户不存在'
        }
      }
      userList.splice(index, 1)
      return {
        code: 200,
        data: {}
      }
    }
  }
]
