# chain call 
```java
 DataMediator<Student> mediator = DataMediatorFactory.createDataMediator(Student.class);
        //���ݴ����
        mediator.getDataProxy()
                .setName(null)
                .setAge(0)
                .setId(0);

        //����������ģ��ʵ��
        mediator.getData().setName(null)
                .setAge(0)
                .setId(0);
```
