<template>
  <div class="designer-container">
    <a-row>
      <a-col :span="1" style="padding-top: 10px; padding-bottom: 10px">
        <a-button type="primary" @click="showProcessInfo">部署流程</a-button>
        <a hidden ref=""></a>
      </a-col>
      <a-col :span="1" style="padding-top: 10px; padding-bottom: 10px; padding-left: 20px">
        <a-button @click="handleUndo">撤销</a-button>
      </a-col>
      <a-col :span="1" style="padding-top: 10px; padding-bottom: 10px; padding-left: 20px">
        <a-button @click="handleRedo">恢复</a-button>
      </a-col>
      <a-col :span="1" style="padding-top: 10px; padding-bottom: 10px; padding-left: 20px">
        <a-button @click="downloadBpmn">保存为BPM</a-button>
      </a-col>
      <a-col :span="1" style="padding-top: 10px; padding-bottom: 10px; padding-left: 60px">
        <a-button @click="downloadSVG">保存为SVG</a-button>
      </a-col>
    </a-row>
    <div id="container" class="containerBox"></div>
    <div id="js-properties-panel" class="panel"></div>
  </div>
</template>

<script lang="ts">
import {defineComponent, markRaw, onMounted, ref} from "vue";
import BpmnModeler from "bpmn-js/lib/Modeler";
import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
} from "bpmn-js-properties-panel";
import 'bpmn-js-properties-panel/dist/assets/element-templates.css'
import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";
import {Button, Row, Col} from "ant-design-vue";
import {customTranslate} from "/@/views/workflow/bpmn/zh";

export default defineComponent({
  name: 'BpmnDesign',
  components: { 'a-button': Button, 'a-row': Row, 'a-col': Col},
  setup() {
    let containerEl: any = ref(null);
    let bpmnModeler: any = ref(null);
    onMounted(async () => {
      init();
    });

    const CustomTranslateModule: any = {
      translate: ['value', customTranslate]
    };

    const init = () => {
      containerEl.value = document.getElementById("container");
      // 加markRaw去除双向绑定作用域
      bpmnModeler.value = markRaw(
          new BpmnModeler({
            container: containerEl.value,
            //添加控制板
            propertiesPanel: {
              parent: "#js-properties-panel"
            },
            additionalModules: [
              // 右边的属性栏
              BpmnPropertiesPanelModule,
              BpmnPropertiesProviderModule,
              CustomTranslateModule
            ],
            moddleExtensions: {
              camunda: camundaModdleDescriptor
            }
          })
      );
      bpmnModeler.value.createDiagram(() => { // 自适应大小
        bpmnModeler.value.get("canvas").zoom("fit-viewport");
      });
    };

    function handleUndo() {
      bpmnModeler.value.get("commandStack").undo();
    }

    function handleRedo() {
      bpmnModeler.value.get("commandStack").redo();
    }

    function handleDownload() {
    }

    async function showProcessInfo() {
      try {
        const result = await bpmnModeler.value.saveXML({ format: true, preamble: true });
        const xml = result.xml;
        console.info(xml);
        // 在这里处理xml数据
      } catch (err) {
        console.error("获取xml失败", err);
      }
    }

    function downloadBpmn() {
      bpmnModeler.value.saveXML({ format: true, preamble: false })
          .then(result => {
            const xml = result.xml;
            const blob = new Blob([xml], { type: "text/xml;charset=utf-8" });
            const url = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "process.bpmn";
            a.click();
            URL.revokeObjectURL(url); // 释放URL对象
          })
          .catch(err => {
            console.error("导出BPMN失败", err);
          });
    }

    function downloadSVG() {
      bpmnModeler.value.saveSVG({ format: true })
          .then(result => {
            const svg = result.svg;
            const blob = new Blob([svg], { type: "text/xml;charset=utf-8" });
            const url = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "process.svg";
            a.click();
            URL.revokeObjectURL(url); // 释放URL对象
          })
          .catch(err => {
            console.error("导出SVG失败", err);
          });
    }

    return {
      containerEl,
      bpmnModeler,
      showProcessInfo,
      handleUndo,
      handleRedo,
      handleDownload,
      downloadBpmn,
      downloadSVG
    }
  }
})

</script>

<style lang="scss">
@import "bpmn-js/dist/assets/diagram-js.css";
@import "bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css";
@import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
@import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';
@import '@bpmn-io/properties-panel/assets/properties-panel.css';

.processDrawBody {
  height: 100%;
  text-align: left;
}

.containerBox {
  width: 100%;
  flex: 1;
  overflow: hidden;
  display: flex;
}

.containerBox #container {
  height: 100%;
  border: 1px solid rgb(121, 121, 121);
}

.bpp-properties-panel [type="text"] {
  box-sizing: border-box;
}

.panel {
  width: 400px;
  position: absolute;
  top: 1px;
  right: 1px;
  height: 100%;
  overflow: auto;
}

/* 右下角logo */
.bjs-powered-by {
  display: none;
}

.designer-container {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
}

.designer-container {
  background-image: linear-gradient(90deg, hsla(0, 0%, 78.4%, .15) 10%, transparent 0), linear-gradient(hsla(0, 0%, 78.4%, .15) 10%, transparent 0);
  background-size: 10px 10px;
}
</style>