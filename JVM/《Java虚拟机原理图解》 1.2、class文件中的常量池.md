# 《Java虚拟机原理图解》 1.2、class文件中的常量池

> **一. 什么是常量池，** **为什么使用常量池**

> **二. 常量池详解（上）**

> - **NO1. 常量池**在class文件的什么位置？
> - **NO2. 常量池**的里面是怎么组织的？
> - **NO3. 常量池项 (cp_info)** 的结构是怎样的？
> - **NO4. 常量池** 能够表示那些信息？
> - **NO5. int和float数据类型的常量**在常量池中是怎样表示和存储的？**
>   **      ( ----介绍 常量池项 CONSTANT_Integer_info, CONSTANT_Float_info)
> - **NO6. long和 double数据类型的常量**在常量池中是怎样表示和存储的？**
>        （**----介绍 常量池项 CONSTANT_Long_info, CONSTANT_Double_info**）**
> - **NO7. \**String类型的字符串常量\****在常量池中是怎样表示和存储的？**
>          (** ----介绍 常量池项 CONSTANT_String_info,CONSTANT_Utf8_info**)**
> - **NO8. 类文件中定义的类名和类中使用到的类**在常量池中是怎样被组织和存储的？
>        **（**----介绍 常量池项 CONSTANT_Class_info**）**

> **三. 常量池详解（下）**

> - **NO9. 类中引用到的field字段**在常量池中是怎样描述的？**
>         (** ----介绍 常量池项  CONSTANT_Fieldref_info**,** CONSTANT_Name_Type_info**)**
> - **NO10. 类中引用到的method方法**在常量池中是怎样被描述的？**
>         \**\*\*\*\*(\*\*\*\*\****----介绍 常量池项 CONSTANT_Methodref_info***\**\*\*\*)\*\*\*\*\****
> - **NO11. 类中引用到某个接口中定义的method方法在常量池中是怎样描述的？**
>         ***\**\*\*\*(\*\*\*\*\****----介绍 常量池项 CONSTANT_InterfaceMethodref_info***\**\*\*\*)\*\*\*\*\****
> - NO12. CONSTANT_MethodType_info
> - NO13. CONSTANT_MethodHandle_info
> - NO13. CONSTANT_InvokeDynamic_info