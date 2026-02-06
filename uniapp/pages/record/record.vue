<template>
  <view class="page-container">
    <!-- 顶部渐变头部 -->
    <view class="header-gradient">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      <view class="decoration-circle circle-3"></view>
      
      <!-- 导航栏 -->
      <view class="navbar">
        <view class="nav-back" @click="goBack">
          <text class="iconfont icon-arrow-left"></text>
        </view>
        <text class="nav-title">{{ isBatchMode ? '批量记账' : '快速记账' }}</text>
        <view class="nav-right"></view>
      </view>

      <!-- 记账模式切换 -->
      <view class="mode-switch-section">
        <view class="mode-switch-tabs">
          <view
            class="mode-switch-tab"
            :class="{ active: !isBatchMode }"
            @click="switchMode(false)"
          >
            <text>单条记账</text>
          </view>
          <view
            class="mode-switch-tab"
            :class="{ active: isBatchMode }"
            @click="switchMode(true)"
          >
            <text>批量记账</text>
          </view>
        </view>
      </view>

    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 记账表单 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-edit"></text>
            </view>
            <text class="header-title">记账信息</text>
          </view>
        </view>
        
        <!-- 单条记账表单 -->
        <template v-if="!isBatchMode">
          <!-- 礼簿名称（只读显示） -->
          <view class="form-item" v-if="selectedGiftBook">
            <view class="item-label">
              <text class="iconfont icon-file-text" style="color: #F59E0B; margin-right: 12rpx;"></text>
              <text>礼簿</text>
            </view>
            <view class="item-value readonly">
              <text>{{ selectedGiftBook.bookName }}</text>
            </view>
          </view>

          <!-- 联系人选择 -->
          <view class="form-item" @click="selectContact">
            <view class="item-label">
              <text class="iconfont icon-user" style="color: #667eea; margin-right: 12rpx;"></text>
              <text>联系人</text>
            </view>
            <view class="item-value" :class="{ placeholder: !selectedContact }">
              <text>{{ selectedContact ? selectedContact.name : '选择或输入姓名' }}</text>
              <text class="iconfont icon-arrow-right" style="margin-left: 12rpx; color: #94A3B8;"></text>
            </view>
          </view>

          <!-- 收支方向 -->
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-zhuanquliuliang" style="color: #8B5CF6; margin-right: 12rpx;"></text>
              <text>收支</text>
            </view>
            <view class="direction-tabs">
              <view 
                class="direction-tab" 
                :class="{ active: transactionDirection === 'income' }"
                @click="transactionDirection = 'income'"
              >
                <text>收入</text>
              </view>
              <view 
                class="direction-tab" 
                :class="{ active: transactionDirection === 'expense' }"
                @click="transactionDirection = 'expense'"
              >
                <text>支出</text>
              </view>
            </view>
          </view>
        </template>

        <!-- 单条记账表单 -->
        <template v-if="!isBatchMode">
          <!-- 支付方式 -->
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-wallet" style="color: #F59E0B; margin-right: 12rpx;"></text>
              <text>方式</text>
            </view>
            <view class="mode-tabs">
              <view
                class="mode-tab"
                :class="{ active: transactionMode === 'cash' }"
                @click="transactionMode = 'cash'"
              >
                <text>现金</text>
              </view>
              <view
                class="mode-tab"
                :class="{ active: transactionMode === 'gift' }"
                @click="transactionMode = 'gift'"
              >
                <text>礼物</text>
              </view>
            </view>
          </view>

          <!-- 金额（现金方式） -->
          <view class="form-item" v-if="transactionMode === 'cash'">
            <view class="item-label">
              <text class="iconfont icon-qianqian" style="color: #10B981; margin-right: 12rpx;"></text>
              <text>金额</text>
            </view>
            <view class="amount-input-wrapper">
              <text class="amount-currency">¥</text>
              <input
                class="amount-input-field"
                type="digit"
                v-model="amount"
                placeholder="0.00"
                @input="onAmountInput"
              />
            </view>
          </view>

          <!-- 礼物信息（礼物方式） -->
          <view class="form-item" v-if="transactionMode === 'gift'">
            <view class="item-label">
              <text class="iconfont icon-gift" style="color: #DB2777; margin-right: 12rpx;"></text>
              <text>礼物</text>
            </view>
            <input
              class="item-input"
              type="text"
              v-model="giftName"
              placeholder="礼物名称"
            />
          </view>

          <!-- 礼物金额（礼物方式） -->
          <view class="form-item" v-if="transactionMode === 'gift'">
            <view class="item-label">
              <text class="iconfont icon-qianqian" style="color: #10B981; margin-right: 12rpx;"></text>
              <text>金额</text>
            </view>
            <view class="amount-input-wrapper">
              <text class="amount-currency">¥</text>
              <input
                class="amount-input-field"
                type="digit"
                v-model="giftValue"
                placeholder="0.00"
                @input="onGiftValueInput"
              />
            </view>
          </view>

          <!-- 备注 -->
          <view class="form-item textarea-item">
            <view class="item-label">
              <text class="iconfont icon-file-text" style="color: #8B5CF6; margin-right: 12rpx;"></text>
              <text>备注</text>
            </view>
            <textarea 
              class="remark-textarea" 
              v-model="remark" 
              placeholder="添加备注信息（可选）"
              maxlength="200"
            />
          </view>
        </template>

        <!-- 批量记账表单 -->
        <view class="batch-records-section" v-if="isBatchMode">
          <view class="batch-records-header">
            <text class="batch-title">记账明细</text>
            <text class="batch-count">共 {{ batchRecords.length }} 条</text>
          </view>

          <!-- 礼簿名称（只读显示） -->
          <view class="form-item" v-if="selectedGiftBook">
            <view class="item-label">
              <text class="iconfont icon-file-text" style="color: #F59E0B; margin-right: 12rpx;"></text>
              <text>礼簿</text>
            </view>
            <view class="item-value readonly">
              <text>{{ selectedGiftBook.bookName }}</text>
            </view>
          </view>

          <view class="batch-record-item" v-for="(record, index) in batchRecords" :key="index">
            <view class="record-header">
              <text class="record-index">#{{ index + 1 }}</text>
              <text class="record-delete" @click="removeBatchRecord(index)">删除</text>
            </view>

            <!-- 联系人 -->
            <view class="form-item" @click="selectBatchContact(index)">
              <view class="item-label">
                <text class="iconfont icon-user" style="color: #667eea; margin-right: 12rpx;"></text>
                <text>联系人</text>
              </view>
              <view class="item-value" :class="{ placeholder: !record.contactName }">
                <text>{{ record.contactName || '选择联系人' }}</text>
                <text class="iconfont icon-arrow-right" style="margin-left: 12rpx; color: #94A3B8;"></text>
              </view>
            </view>

            <!-- 收支 -->
            <view class="form-item">
              <view class="item-label">
                <text class="iconfont icon-zhuanquliuliang" style="color: #8B5CF6; margin-right: 12rpx;"></text>
                <text>收支</text>
              </view>
              <view class="direction-tabs">
                <view
                  class="direction-tab"
                  :class="{ active: (record.transactionDirection || 'income') === 'income' }"
                  @click="updateBatchRecord(index, 'transactionDirection', 'income')"
                >
                  <text>收入</text>
                </view>
                <view
                  class="direction-tab"
                  :class="{ active: (record.transactionDirection || 'income') === 'expense' }"
                  @click="updateBatchRecord(index, 'transactionDirection', 'expense')"
                >
                  <text>支出</text>
                </view>
              </view>
            </view>

            <!-- 方式 -->
            <view class="form-item">
              <view class="item-label">
                <text class="iconfont icon-wallet" style="color: #F59E0B; margin-right: 12rpx;"></text>
                <text>方式</text>
              </view>
              <view class="mode-tabs">
                <view
                  class="mode-tab"
                  :class="{ active: (record.transactionMode || 'cash') === 'cash' }"
                  @click="updateBatchRecord(index, 'transactionMode', 'cash')"
                >
                  <text>现金</text>
                </view>
                <view
                  class="mode-tab"
                  :class="{ active: (record.transactionMode || 'cash') === 'gift' }"
                  @click="updateBatchRecord(index, 'transactionMode', 'gift')"
                >
                  <text>礼物</text>
                </view>
              </view>
            </view>

            <!-- 金额（现金方式） -->
            <view class="form-item" v-if="(record.transactionMode || 'cash') === 'cash'">
              <view class="item-label">
                <text class="iconfont icon-qianqian" style="color: #10B981; margin-right: 12rpx;"></text>
                <text>金额</text>
              </view>
              <view class="amount-input-wrapper">
                <text class="amount-currency">¥</text>
                <input 
                  class="amount-input-field" 
                  type="digit" 
                  :value="record.amount"
                  @input="e => updateBatchRecord(index, 'amount', filterNumberInput(e.detail.value))"
                  placeholder="0.00"
                />
              </view>
            </view>

            <!-- 礼物信息（礼物方式） -->
            <view class="form-item" v-if="(record.transactionMode || 'cash') === 'gift'">
              <view class="item-label">
                <text class="iconfont icon-gift" style="color: #DB2777; margin-right: 12rpx;"></text>
                <text>礼物</text>
              </view>
              <input 
                class="item-input" 
                type="text" 
                :value="record.giftName"
                @input="e => updateBatchRecord(index, 'giftName', e.detail.value)"
                placeholder="礼物名称"
              />
            </view>

            <!-- 礼物金额（礼物方式） -->
            <view class="form-item" v-if="(record.transactionMode || 'cash') === 'gift'">
              <view class="item-label">
                <text class="iconfont icon-qianqian" style="color: #10B981; margin-right: 12rpx;"></text>
                <text>金额</text>
              </view>
              <view class="amount-input-wrapper">
                <text class="amount-currency">¥</text>
                <input 
                  class="amount-input-field" 
                  type="digit" 
                  :value="record.giftValue"
                  @input="e => updateBatchRecord(index, 'giftValue', filterNumberInput(e.detail.value))"
                  placeholder="0.00"
                />
              </view>
            </view>

            <!-- 备注 -->
            <view class="form-item">
              <view class="item-label">
                <text class="iconfont icon-file-text" style="color: #8B5CF6; margin-right: 12rpx;"></text>
                <text>备注</text>
              </view>
              <input 
                class="item-input" 
                type="text" 
                :value="record.remark"
                @input="e => updateBatchRecord(index, 'remark', e.detail.value)"
                placeholder="备注（可选）"
              />
            </view>
          </view>

          <!-- 添加记录按钮 -->
          <view class="add-record-btn" @click="addBatchRecord">
            <text class="iconfont icon-plus" style="margin-right: 8rpx;"></text>
            <text>添加一条记录</text>
          </view>
        </view>
      </view>
	  
      <!-- 保存按钮 -->
      <view style="padding: 40rpx 0;">
        <button class="btn-primary" :class="{ 'loading': loading }" @click="handleSave" :disabled="loading || (!isBatchMode && !canSave)">
          <text v-if="!loading">{{ isBatchMode ? '批量保存' : '保存记账' }}</text>
          <view v-else class="loading-spinner"></view>
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import transactionApi from '@/api/transaction'
import contactApi from '@/api/contact'
import giftbookApi from '@/api/giftbook'

export default {
  data() {
    return {
      amount: '',
      selectedContact: null,
      transactionDate: '',
      remark: '',
      transactionDirection: 'expense',
      transactionMode: 'cash',
      giftName: '',
      giftValue: '',
      selectedGiftBook: null,
      giftBookId: null,
      loading: false,
      giftBooks: [],
      fromPage: '', // 来源页面：giftbook-礼簿管理页面
      isBatchMode: false, // 是否为批量记账模式
      batchRecords: [], // 批量记账记录列表
      currentBatchIndex: -1 // 当前选择的批量记录索引
    }
  },
  
  computed: {
    canSave() {
      if (!this.selectedContact) {
        return false
      }

      if (this.transactionMode === 'cash') {
        return this.amount && parseFloat(this.amount) > 0
      } else {
        return this.giftName
      }
    }
  },
  
  onLoad(options) {
    // 设置来源页面
    this.fromPage = options.from || ''
    
    // 检查本地存储中是否有选择的礼簿ID
    const selectedGiftBookId = uni.getStorageSync('selectedGiftBookId')
    const selectedGiftBook = uni.getStorageSync('selectedGiftBook')
    const selectedFromPage = uni.getStorageSync('selectedFromPage')
    
    if (selectedGiftBookId) {
      // 使用本地存储中的礼簿ID
      this.giftBookId = selectedGiftBookId
      this.selectedGiftBook = selectedGiftBook
      this.fromPage = selectedFromPage || ''
      // 使用礼簿的日期作为交易日期
      if (selectedGiftBook && selectedGiftBook.bookDate) {
        this.transactionDate = selectedGiftBook.bookDate
      }
      // 清除本地存储
      uni.removeStorageSync('selectedGiftBookId')
      uni.removeStorageSync('selectedGiftBook')
      uni.removeStorageSync('selectedFromPage')
      return
    }

    // 如果没有传入礼簿ID，先跳转到礼簿选择页面
    if (!options.giftBookId) {
      uni.switchTab({
        url: '/pages/giftbook-select/giftbook-select?from=record'
      })
      return
    }

    // 加载礼簿详情
    this.giftBookId = options.giftBookId
    this.loadGiftBookDetail(options.giftBookId)
  },

  onShow() {
    // 检查本地存储中是否有选择的礼簿ID（用于 switchTab 跳转的情况）
    const selectedGiftBookId = uni.getStorageSync('selectedGiftBookId')
    const selectedGiftBook = uni.getStorageSync('selectedGiftBook')
    const selectedFromPage = uni.getStorageSync('selectedFromPage')

    if (selectedGiftBookId) {
      // 使用本地存储中的礼簿ID
      this.giftBookId = selectedGiftBookId
      this.selectedGiftBook = selectedGiftBook
      this.fromPage = selectedFromPage || ''
      // 使用礼簿的日期作为交易日期
      if (selectedGiftBook && selectedGiftBook.bookDate) {
        this.transactionDate = selectedGiftBook.bookDate
      }
      // 清除本地存储
      uni.removeStorageSync('selectedGiftBookId')
      uni.removeStorageSync('selectedGiftBook')
      uni.removeStorageSync('selectedFromPage')
    } else if (!this.giftBookId) {
      // 如果没有礼簿ID，跳转到礼簿选择页面
      uni.switchTab({
        url: '/pages/giftbook-select/giftbook-select?from=record'
      })
    }
  },
  
  methods: {
    switchMode(isBatch) {
      this.isBatchMode = isBatch
      if (isBatch && this.batchRecords.length === 0) {
        // 切换到批量模式时，初始化一条记录
        this.addBatchRecord()
      }
    },

    addBatchRecord() {
      this.batchRecords.push({
        contactName: '',
        selectedContact: null,
        transactionDirection: 'income',
        transactionMode: 'cash',
        amount: '',
        giftName: '',
        giftValue: '',
        remark: ''
      })
    },

    removeBatchRecord(index) {
      this.batchRecords.splice(index, 1)
      if (this.batchRecords.length === 0) {
        this.addBatchRecord()
      }
    },

    updateBatchRecord(index, field, value) {
      this.$set(this.batchRecords[index], field, value)
    },
    
    handleAmountInput(e) {
      let value = e.detail.value
      value = value.replace(/[^\d.]/g, '')
      const parts = value.split('.')
      if (parts.length > 2) {
        value = parts[0] + '.' + parts.slice(1).join('')
      }
      if (parts[1] && parts[1].length > 2) {
        value = parts[0] + '.' + parts[1].substring(0, 2)
      }
      this.amount = value
    },

    onAmountInput(e) {
      // 过滤非数字字符（只允许数字和小数点）
      let value = e.detail.value
      // 移除所有非数字和非小数点字符
      value = value.replace(/[^\d.]/g, '')
      // 确保只有一个小数点
      const parts = value.split('.')
      if (parts.length > 2) {
        value = parts[0] + '.' + parts.slice(1).join('')
      }
      this.amount = value
    },

    onGiftValueInput(e) {
      // 过滤非数字字符（只允许数字和小数点）
      let value = e.detail.value
      // 移除所有非数字和非小数点字符
      value = value.replace(/[^\d.]/g, '')
      // 确保只有一个小数点
      const parts = value.split('.')
      if (parts.length > 2) {
        value = parts[0] + '.' + parts.slice(1).join('')
      }
      this.giftValue = value
    },

    filterNumberInput(value) {
      // 过滤非数字字符（只允许数字和小数点）
      value = value.replace(/[^\d.]/g, '')
      // 确保只有一个小数点
      const parts = value.split('.')
      if (parts.length > 2) {
        value = parts[0] + '.' + parts.slice(1).join('')
      }
      return value
    },

    selectContact() {
      uni.navigateTo({
        url: '/pages/contacts/contacts?select=true'
      })
    },

    handleSelectContact(contact) {
      this.selectedContact = contact
    },

    selectBatchContact(index) {
      this.currentBatchIndex = index
      uni.navigateTo({
        url: '/pages/contacts/contacts?select=true&batch=true'
      })
    },

    handleSelectBatchContact(contact) {
      const index = this.currentBatchIndex
      if (index !== undefined && index >= 0) {
        this.$set(this.batchRecords[index], 'contactName', contact.name)
        this.$set(this.batchRecords[index], 'selectedContact', contact)
      }
    },

    onDateChange(e) {
      this.transactionDate = e.detail.value
    },

    selectGiftBook() {
      // 跳转到礼簿选择页面（tabBar页面）
      uni.switchTab({
        url: '/pages/giftbook-select/giftbook-select'
      })
    },
    
    handleSelectGiftBook(giftBook) {
      this.selectedGiftBook = giftBook
    },
    
    async loadGiftBookDetail(id) {
      try {
        const res = await giftbookApi.getGiftBookDetail({ id })
        if (res.code === 1) {
          this.selectedGiftBook = res.data
          // 使用礼簿的日期作为交易日期
          if (res.data && res.data.bookDate) {
            this.transactionDate = res.data.bookDate
          }
        }
      } catch (error) {
        console.error('加载礼簿详情失败:', error)
      }
    },
    
    async loadGiftBooks() {
      try {
        const res = await giftbookApi.getActiveGiftBooks()
        if (res.code === 1) {
          this.giftBooks = res.data || []
        }
      } catch (error) {
        console.error('加载礼簿失败:', error)
      }
    },
    
    validateForm() {
      if (!this.transactionDirection) {
        uni.showToast({
          title: '请选择收支方向',
          icon: 'none'
        })
        return false
      }
      
      if (!this.transactionMode) {
        uni.showToast({
          title: '请选择支付方式',
          icon: 'none'
        })
        return false
      }
      
      if (this.transactionMode === 'cash') {
        if (!this.amount) {
          uni.showToast({
            title: '请输入金额',
            icon: 'none'
          })
          return false
        }
        
        if (parseFloat(this.amount) <= 0) {
          uni.showToast({
            title: '金额必须大于0',
            icon: 'none'
          })
          return false
        }
      } else {
        if (!this.giftName) {
          uni.showToast({
            title: '请输入礼物名称',
            icon: 'none'
          })
          return false
        }
      }
      
      if (!this.selectedContact) {
        uni.showToast({
          title: '请选择对象',
          icon: 'none'
        })
        return false
      }

      if (!this.transactionDate) {
        uni.showToast({
          title: '请选择礼簿以获取日期',
          icon: 'none'
        })
        return false
      }

      return true
    },

    validateBatchForm() {
      for (let i = 0; i < this.batchRecords.length; i++) {
        const record = this.batchRecords[i]

        if (!record.contactName) {
          uni.showToast({
            title: `第${i + 1}条记录请选择联系人`,
            icon: 'none'
          })
          return false
        }

        const mode = record.transactionMode || 'cash'
        if (mode === 'cash') {
          if (!record.amount) {
            uni.showToast({
              title: `第${i + 1}条记录请输入金额`,
              icon: 'none'
            })
            return false
          }
          if (parseFloat(record.amount) <= 0) {
            uni.showToast({
              title: `第${i + 1}条记录金额必须大于0`,
              icon: 'none'
            })
            return false
          }
        } else {
          if (!record.giftName) {
            uni.showToast({
              title: `第${i + 1}条记录请输入礼物名称`,
              icon: 'none'
            })
            return false
          }
        }
      }

      return true
    },

    async handleSave() {
      if (this.isBatchMode) {
        await this.handleBatchSave()
      } else {
        await this.handleSingleSave()
      }
    },

    async handleSingleSave() {
      if (!this.validateForm()) {
        return
      }

      this.loading = true

      try {
        const data = {
          contactId: this.selectedContact.id,
          contactName: this.selectedContact.name,
          relationType: this.selectedContact.relationType,
          transactionDirection: this.transactionDirection,
          transactionMode: this.transactionMode,
          transactionDate: this.transactionDate,
          remark: this.remark
        }

        if (this.transactionMode === 'cash') {
          data.amount = parseFloat(this.amount)
        } else {
          data.giftName = this.giftName
          if (this.giftValue) {
            data.giftValue = parseFloat(this.giftValue)
          }
        }

        if (this.selectedGiftBook) {
          data.giftBookId = this.selectedGiftBook.id
        }

        const res = await transactionApi.addTransaction(data)

        if (res.code === 1) {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })

          this.resetForm()

          // 保存礼簿ID用于跳转
          const currentGiftBookId = this.giftBookId

          // 清除礼簿ID，下次进入需要重新选择
          this.giftBookId = null
          this.selectedGiftBook = null

          setTimeout(() => {
            if (currentGiftBookId) {
              // 无论从哪里进入，保存成功后都跳转到礼簿详情页
              uni.navigateTo({
                url: `/pages/giftbook-detail/giftbook-detail?id=${currentGiftBookId}`
              })
            } else {
              // 没有礼簿ID时跳转到首页
              uni.switchTab({
                url: '/pages/index/index'
              })
            }
          }, 1500)
        }
      } catch (error) {
        console.error('保存失败:', error)
        uni.showToast({
          title: '保存失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },

    async handleBatchSave() {
      if (!this.validateBatchForm()) {
        return
      }

      this.loading = true

      try {
        const records = this.batchRecords.map(record => {
          const data = {
            contactId: record.selectedContact ? record.selectedContact.id : null,
            contactName: record.contactName,
            relationType: record.selectedContact ? record.selectedContact.relationType : '',
            transactionDirection: record.transactionDirection || 'income',
            transactionMode: record.transactionMode || 'cash',
            transactionDate: this.transactionDate,
            remark: record.remark || ''
          }

          if (data.transactionMode === 'cash') {
            data.amount = parseFloat(record.amount) || 0
          } else {
            data.giftName = record.giftName
            if (record.giftValue) {
              data.giftValue = parseFloat(record.giftValue)
            }
          }

          if (this.selectedGiftBook) {
            data.giftBookId = this.selectedGiftBook.id
          }

          return data
        })

        const res = await transactionApi.batchAddTransactions(records)

        if (res.code === 1) {
          uni.showToast({
            title: `成功保存 ${records.length} 条记录`,
            icon: 'success'
          })

          // 保存礼簿ID用于跳转
          const currentGiftBookId = this.giftBookId

          // 清除礼簿ID，下次进入需要重新选择
          this.giftBookId = null
          this.selectedGiftBook = null

          this.resetForm()

          setTimeout(() => {
            if (currentGiftBookId) {
              // 无论从哪里进入，保存成功后都跳转到礼簿详情页
              uni.navigateTo({
                url: `/pages/giftbook-detail/giftbook-detail?id=${currentGiftBookId}`
              })
            } else {
              // 没有礼簿ID时跳转到首页
              uni.switchTab({
                url: '/pages/index/index'
              })
            }
          }, 1500)
        }
      } catch (error) {
        console.error('批量保存失败:', error)
        uni.showToast({
          title: '批量保存失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    handleVoice() {
      uni.showToast({
        title: '语音功能开发中',
        icon: 'none'
      })
    },
    
    handleImage() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          console.log('选择的图片:', res.tempFilePaths[0])
          uni.showToast({
            title: '图片上传功能开发中',
            icon: 'none'
          })
        }
      })
    },
    
    resetForm() {
      this.amount = ''
      this.selectedContact = null
      this.remark = ''
      this.giftName = ''
      this.giftValue = ''
      this.batchRecords = []
      this.isBatchMode = false
      this.currentBatchIndex = -1
      // 重置日期为当前礼簿的日期
      if (this.selectedGiftBook && this.selectedGiftBook.bookDate) {
        this.transactionDate = this.selectedGiftBook.bookDate
      }
    },
    
    goToContacts() {
      uni.navigateTo({
        url: '/pages/contacts/contacts'
      })
    },
    
    goBack() {
      // 清除礼簿ID，下次进入需要重新选择
      this.giftBookId = null
      this.selectedGiftBook = null

      // 根据来源页面决定返回哪里
      if (this.fromPage === 'giftbook') {
        // 从礼簿管理页面进入，返回到礼簿管理页面
        uni.switchTab({
          url: '/pages/giftbook/giftbook'
        })
      } else if (this.fromPage === 'giftbook-detail') {
        // 从礼簿详情页面进入，返回礼簿详情页
        uni.navigateBack()
      } else if (this.fromPage === 'giftbook-select') {
        // 从礼簿选择页面（tabBar记账）进入，返回到首页
        uni.switchTab({
          url: '/pages/index/index'
        })
      } else {
        // 默认返回到首页
        uni.switchTab({
          url: '/pages/index/index'
        })
      }
    },

    getRelationIcon(relationType) {
      const iconMap = {
        '亲戚': 'icon-heart',
        '朋友': 'icon-users',
        '同事': 'icon-message-square',
        '同学': 'icon-star',
        '其他': 'icon-more'
      }
      return iconMap[relationType] || 'icon-user'
    },

    getRelationIconClass(relationType) {
      const classMap = {
        '亲戚': 'family',
        '朋友': 'friend',
        '同事': 'colleague',
        '同学': 'classmate',
        '其他': 'other'
      }
      return classMap[relationType] || 'default'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

// 金额输入区
.amount-section {
  padding: 40rpx $page-padding 80rpx;
  
  .amount-label {
    font-size: $font-size-base;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: $spacing-sm;
    display: block;
  }
  
  .amount-display {
    display: flex;
    align-items: baseline;
    
    .currency {
      font-size: 48rpx;
      font-weight: $font-weight-bold;
      color: #FFFFFF;
      margin-right: $spacing-sm;
    }
    
    .amount-input {
      flex: 1;
      font-size: 80rpx;
      font-weight: $font-weight-bold;
      color: #FFFFFF;
      background: transparent;
      height: 100rpx;
      line-height: 100rpx;
      padding: 0;
      margin: 0;
      
      &::placeholder {
        color: rgba(255, 255, 255, 0.4);
      }
    }
  }
}

// 表单样式
.form-item {
  display: flex;
  align-items: center;
  padding: $spacing-md 0;
  border-bottom: 1rpx solid $bg-secondary;
  
  &:last-child {
    border-bottom: none;
  }
  
  &.textarea-item {
    flex-direction: column;
    align-items: flex-start;
    
    .item-label {
      margin-bottom: $spacing-sm;
    }
  }
  
  .item-label {
    display: flex;
    align-items: center;
    font-size: $font-size-base;
    color: $text-primary;
    font-weight: $font-weight-medium;
    min-width: 140rpx;
  }
  
  .item-value {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    font-size: $font-size-base;
    color: $text-primary;

    &.placeholder {
      color: $text-tertiary;
    }

    &.readonly {
      color: $text-secondary;
    }
  }
  
  .remark-textarea {
    width: 100%;
    min-height: 160rpx;
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: $spacing-md;
    font-size: $font-size-base;
    color: $text-primary;
  }
  
  .item-input {
    flex: 1;
    text-align: right;
    font-size: $font-size-base;
    color: $text-primary;
  }

  // 金额输入样式
  .amount-input-wrapper {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: flex-end;

    .amount-currency {
      font-size: 32rpx;
      font-weight: $font-weight-bold;
      color: $text-primary;
      margin-right: 8rpx;
    }

    .amount-input-field {
      flex: 1;
      text-align: right;
      font-size: 36rpx;
      font-weight: $font-weight-bold;
      color: $text-primary;
      max-width: 200rpx;

      &::placeholder {
        color: $text-tertiary;
      }
    }
  }

  .direction-tabs {
    flex: 1;
    display: flex;
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: 4rpx;
    gap: 4rpx;
    
    .direction-tab {
      flex: 1;
      text-align: center;
      padding: 12rpx 0;
      border-radius: $radius-md;
      font-size: $font-size-base;
      color: $text-secondary;
      transition: all 0.3s ease;
      
      &.active {
        background: #FFFFFF;
        color: $text-primary;
        font-weight: $font-weight-medium;
        box-shadow: $shadow-sm;
      }
    }
  }
  
  .mode-tabs {
    flex: 1;
    display: flex;
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: 4rpx;
    gap: 4rpx;
    
    .mode-tab {
      flex: 1;
      text-align: center;
      padding: 12rpx 0;
      border-radius: $radius-md;
      font-size: $font-size-base;
      color: $text-secondary;
      transition: all 0.3s ease;
      
      &.active {
        background: #FFFFFF;
        color: $text-primary;
        font-weight: $font-weight-medium;
        box-shadow: $shadow-sm;
      }
    }
  }
}

// 记账模式切换
.mode-switch-section {
  padding: 20rpx 40rpx;

  .mode-switch-tabs {
    display: flex;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 32rpx;
    padding: 4rpx;
    gap: 4rpx;

    .mode-switch-tab {
      flex: 1;
      text-align: center;
      padding: 16rpx 0;
      border-radius: 28rpx;
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.8);
      transition: all 0.3s ease;

      &.active {
        background: #FFFFFF;
        color: #667eea;
        font-weight: 500;
      }
    }
  }
}

// 批量记账区域
.batch-records-section {
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 2rpx dashed #E2E8F0;

  .batch-records-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .batch-title {
      font-size: 30rpx;
      font-weight: 600;
      color: #1E293B;
    }

    .batch-count {
      font-size: 26rpx;
      color: #64748B;
    }
  }

  .batch-record-item {
    background: #F8FAFC;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;

    .record-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16rpx;
      padding-bottom: 16rpx;
      border-bottom: 1rpx solid #E2E8F0;

      .record-index {
        font-size: 28rpx;
        font-weight: 600;
        color: #667eea;
      }

      .record-delete {
        font-size: 26rpx;
        color: #EF4444;
        padding: 8rpx 16rpx;
      }
    }

    .form-item {
      padding: 12rpx 0;

      &:last-child {
        border-bottom: none;
      }
    }
  }

  .add-record-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 24rpx;
    background: #F1F5F9;
    border-radius: 16rpx;
    border: 2rpx dashed #CBD5E1;
    margin-top: 20rpx;

    &:active {
      background: #E2E8F0;
    }

    text {
      font-size: 28rpx;
      color: #64748B;
    }
  }
}

// 快捷操作
.quick-actions-row {
  display: flex;
  justify-content: space-around;
  padding: $spacing-lg 0;
  margin-bottom: $spacing-md;
  
  .quick-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: $spacing-sm;
    
    &:active {
      opacity: 0.7;
    }
  }
  
  .quick-label {
    font-size: $font-size-sm;
    color: $text-secondary;
  }
}

// 保存按钮禁用状态
.btn-primary[disabled] {
  opacity: 0.6;
  transform: none !important;
}
</style>
