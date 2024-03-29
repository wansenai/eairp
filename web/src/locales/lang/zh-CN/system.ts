export default {
    selectData: '请选择一条数据',
    modifyDataPrompt: '抱歉，只有未审核的单据才能编辑！',
    role: {
        title: '角色列表',
        addRole: '新增角色',
        editRole: '编辑角色',
        menuAllocation: '菜单分配',
        titleNotice: '角色列表可以给角色赋予不同的权限, 点击操作栏中的齿轮按钮',
        header: {
            roleName: '角色名称',
            status: '状态',
            viewAllData: '查看全部数据',
            allData: '全部数据',
            viewPersonalData: '查看个人数据',
            personalData: '个人数据',
            blockPurchasePrice: '屏蔽采购价',
            blockSalesPrice: '屏蔽销售价',
            blockRetailPrice: '屏蔽零售价',
            enable: '启用',
            disable: '停用',
        },
        table: {
            roleName: '角色名称',
            type: '类型',
            priceBlocking: '价格屏蔽',
            status: '状态',
            remark: '备注',
            createTime: '创建时间',
            action: '操作',
        },
        form: {
            roleName: '角色名称',
            type: '类型',
            priceBlocking: '价格屏蔽',
            status: '状态',
            remark: '备注',
        }
    },
    department: {
        title: '部门列表',
        addDepartment: '新增部门',
        editDepartment: '编辑部门',
        header: {
          name: '部门名称',
        },
        table: {
            name: '部门名称',
            number: '部门编号',
            manager: '部门负责人',
            status: '状态',
            createTime: '创建时间',
            remark: '备注',
            action: '操作',
        },
        form: {
            name: '部门名称',
            number: '部门编号',
            parent: '上级部门',
            manager: '部门负责人',
            status: '状态',
            sort: '排序',
            remark: '备注',
            enable: '启用',
            disable: '停用',
            notice: '如果不填写，则默认为父级部门',
        }
    },
    menu: {
        title: '菜单列表',
        addMenu: '新增菜单',
        editMenu: '编辑菜单',
        table: {
            menuTitle: '菜单标题',
            icon: '图标',
            path: '路径',
            component: '组件',
            sort: '排序',
            status: '状态',
            createTime: '创建时间',
        },
        form:{
            menuType: '菜单类型',
            catalogue: '目录',
            menu: '菜单',
            rootMenu: '根菜单',
            menuName: '菜单名称',
            menuTitle: '菜单标题',
            menuEnglishTitle: '菜单英文标题',
            parent: '上级菜单',
            sort: '排序',
            icon: '图标',
            routeAddress: '路由地址',
            componentPath: '组件路径',
            status: '状态',
            enable: '启用',
            disable: '停用',
            isExternalLink: '是否外链',
            isCached: '是否缓存',
            isDisplayed: '是否显示',
            yes: '是',
            no: '否',
            notice: '如果不填写，则默认为目录'
        }
    },
    configure: {
        title: '系统配置',
        tip: '此页面功能主要对当前系统进行一些配置。',
        name: '公司名称',
        inputName: '请输入公司名称',
        contact: '联系人',
        inputContact: '请输入联系人',
        address: '公司地址',
        inputAddress: '请输入公司地址',
        phone: '公司电话',
        inputPhone: '请输入公司电话',
        fax: '公司传真',
        inputFax: '请输入公司传真',
        postalCode: '邮编',
        inputPostalCode: '请输入邮编',
        salesProtocol: '销售协议',
        inputSalesProtocol: '请输入销售协议',
        noticeOne: '更换公司名称后，请刷新浏览器生效',
        noticeTwo: '选填',
        submit: '提交',
        updateSuccess: '更新成功',
        updateFailed: '更新失败',
    }
}