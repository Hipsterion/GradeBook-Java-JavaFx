//package BusinessLayer.factories;
//
//import BusinessLayer.domain.Student;
//import serviceLayer.validators.StudentValidator;
//import serviceLayer.validators.TemaValidator;
//import serviceLayer.validators.Validator;
//
//import java.lang.reflect.ParameterizedType;
//
//public class ValidatorFactory {
//    public static ValidatorFactory validator = null;
//    private ValidatorFactory(){}
//
//    public static ValidatorFactory getInstance(){
//        if(validator == null) validator = new ValidatorFactory();
//        return validator;
//    }
//    public <E> Validator getValidator(){
//        String typename = ((Class<E>) ((ParameterizedType) getClass()
//                .getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName(); //typeof(E).FullNameType;
//        switch(typename){
//            case "Student":
//                return new StudentValidator();
//            case "Tema":
//                return new TemaValidator();
//            default:
//                return null;
//        }
//    }
//}


//typeOf(E).FullNameType();
