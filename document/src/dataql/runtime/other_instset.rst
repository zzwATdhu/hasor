其它指令
------------------------------------
LABEL
'''''''
协助GOTO定位用，无实际作用

* 参数说明：共0参数；
* 栈行为：消费0，产出0
* 堆行为：无

.. code-block:: text
    :linenos:

    代码：if (1 == 1) return true; else return false
    指令：
      ...(部分略)...
      #02  DO        ==
      #03  IF        7   <- 如果判断失败那么跳转到 #07 行指令
      ...(部分略)...
      #07  LABEL     7   <- LABEL 7 之后的相当于 else 部分
      ...(部分略)...

LINE
'''''''
行号，无实际作用

* 参数说明：共0参数；
* 栈行为：消费0，产出0
* 堆行为：无
