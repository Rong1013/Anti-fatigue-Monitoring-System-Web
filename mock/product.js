const Mock = require('mockjs')
const { Random } = Mock

// 生成20条产品数据
const productList = [];
for (let i = 1; i <= 20; i++) {
  productList.push({
    id: i,
    name: `齿轮-${Random.integer(100, 999)}`,
    category: Random.pick(['粗加工', '精加工', '热处理', '表面处理']),
    categoryName: Random.word(3, 8),
    deviceCount: Random.integer(1, 50),
    status: Random.pick(['已启用', '未启用']),
    leader: Random.cname(),
    createTime: Random.date('yyyy-MM-dd', new Date(new Date().getFullYear(), 0, 1), new Date())
  });
}

module.exports = [
  {
    url: '/vue-admin-template/product/list',
    type: 'get',
    response: config => {
      const { page = 1, pageSize = 10, name, category } = config.query;
      // 过滤逻辑：根据名称和分类搜索
      const filteredList = productList.filter(item => {
        return (!name || item.name.includes(name)) &&
               (!category || item.category.includes(category));
      });
      // 分页逻辑
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
  }
]