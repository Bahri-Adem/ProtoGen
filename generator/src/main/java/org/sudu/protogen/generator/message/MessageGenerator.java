package org.sudu.protogen.generator.message;

import com.squareup.javapoet.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.sudu.protogen.descriptors.EnumOrMessage;
import org.sudu.protogen.descriptors.Message;
import org.sudu.protogen.generator.DescriptorGenerator;
import org.sudu.protogen.generator.GenerationContext;
import org.sudu.protogen.generator.field.FieldGenerationHelper;
import org.sudu.protogen.generator.field.FieldProcessingResult;
import org.sudu.protogen.utils.Name;
import javax.lang.model.element.Modifier;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import org.sudu.protogen.utils.Poem;

import javax.lang.model.element.Modifier;
import javax.persistence.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MessageGenerator implements DescriptorGenerator<Message, TypeSpec> {

    private final GenerationContext generationContext;

    public MessageGenerator(@NotNull GenerationContext generationContext) {
        this.generationContext = generationContext;
    }

    @NotNull
    public TypeSpec generate(@NotNull Message msgDescriptor) {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(generatedType(msgDescriptor).simpleName());

        List<FieldProcessingResult> processedFields = msgDescriptor.getFields().stream()
                .map(field -> generationContext.generatorsHolder().generate(field))
                .filter(FieldProcessingResult::isNonVoid)
                .toList();
        addAnnotations(msgDescriptor, typeBuilder);
        addFields(msgDescriptor,typeBuilder);
        addSetters(msgDescriptor,typeBuilder);
        addComponents(processedFields, typeBuilder);
        addConstructor(msgDescriptor,typeBuilder);
        addEmptyConstructor(msgDescriptor,typeBuilder);
        //implementComparable(msgDescriptor, typeBuilder);
        //addTopicField(msgDescriptor, typeBuilder);
        addTransformingMethods(msgDescriptor, processedFields, typeBuilder);
        addOneofs(msgDescriptor, typeBuilder);
       // addBuilderIfNecessary(msgDescriptor, typeBuilder);

        return typeBuilder
                .addModifiers(Modifier.PUBLIC)
                .addTypes(generateNested(msgDescriptor))
                .build();
    }
    private void addAnnotations(Message descriptor, TypeSpec.Builder builder) {
        AnnotationSpec entityAnnotation = AnnotationSpec.builder(Entity.class).build();
        builder.addAnnotation(entityAnnotation);
        AnnotationSpec dataAnnotation = AnnotationSpec.builder(Data.class).build();
        builder.addAnnotation(dataAnnotation);
        String className = descriptor.getDomainTypeName(generationContext.configuration().namingManager()).simpleName().toLowerCase()+"s";
        AnnotationSpec tableAnnotation = AnnotationSpec.builder(Table.class)
                .addMember("name", "$S", "\"" + className + "\"")
                .build();
        builder.addAnnotation(tableAnnotation);
    }
    private void addConstructor(Message descriptor, TypeSpec.Builder builder) {
        List<FieldSpec> nonNullFields = FieldGenerationHelper.processAllFields(descriptor, generationContext)
                .map(FieldProcessingResult::field)
                .map(field -> field.toBuilder().clearAnnotations().build())
                .toList();

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        // Add parameters to the constructor
        nonNullFields.forEach(field ->
                constructorBuilder.addParameter(Poem.fieldToParameter(field))
        );

        // Generate constructor body to initialize fields
        nonNullFields.forEach(field ->
                constructorBuilder.addStatement("this.$N = $N", field, field)
        );

        // Build the constructor
        MethodSpec constructor = constructorBuilder.build();

        // Add the constructor to the builder
        builder.addMethod(constructor);
    }
    private void addEmptyConstructor(Message descriptor, TypeSpec.Builder builder) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
        // Build the constructor
        MethodSpec constructor = constructorBuilder.build();
        // Add the constructor to the builder
        builder.addMethod(constructor);
    }


    private void addFields(Message descriptor, TypeSpec.Builder builder) {
        FieldGenerationHelper.processAllFields(descriptor, generationContext)
                .map(FieldProcessingResult::field)
                .map(field -> {
                    FieldSpec.Builder fieldBuilder = field.toBuilder().clearAnnotations().addModifiers(Modifier.PRIVATE);
                    if (field.getName().equals("id")) {
                        // Add annotations for id field
                        AnnotationSpec idAnnotation = AnnotationSpec.builder(Id.class).build();
                        AnnotationSpec generatedValueAnnotation = AnnotationSpec.builder(GeneratedValue.class)
                                .addMember("strategy", "$T.AUTO", GenerationType.class)
                                .build();
                        fieldBuilder.addAnnotation(idAnnotation)
                                .addAnnotation(generatedValueAnnotation);
                    }
                    return fieldBuilder.build();
                })
                .forEach(builder::addField);
    }
    private String getBuilderName(Message descriptor) {
        return descriptor.getDomainTypeName(generationContext.configuration().namingManager()).simpleName() + "Builder";
    }
    private void addSetters(Message descriptor, TypeSpec.Builder builder) {
        String builderName = "void";
        FieldGenerationHelper.processAllFields(descriptor, generationContext)
                .filter(FieldProcessingResult::isNullable)
                .map(FieldProcessingResult::field)
                .map(field -> setterForField(field, builderName))
                .forEach(builder::addMethod);
    }

    @NotNull
    private MethodSpec setterForField(FieldSpec fieldSpec, String builderName) {
        return MethodSpec.methodBuilder("set" + Name.toCamelCase(fieldSpec.name))
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("",builderName))
                .addParameter(Poem.fieldToParameter(fieldSpec))
                .addStatement("this.$N = $N", fieldSpec, fieldSpec)
                //.addStatement("return this")
                .build();
    }
    private void addBuilderIfNecessary(Message msgDescriptor, TypeSpec.Builder typeBuilder) {
        if (!msgDescriptor.generateBuilderOption()) return;
        List<FieldProcessingResult> processedFields = FieldGenerationHelper.processAllFields(msgDescriptor, generationContext).toList();
        if (processedFields.stream().anyMatch(FieldProcessingResult::isNullable)) {
            typeBuilder.addType(generationContext.generatorsHolder().generateBuilder(msgDescriptor));
            List<FieldSpec> notNullFields = processedFields.stream().filter(Predicate.not(FieldProcessingResult::isNullable)).map(FieldProcessingResult::field).toList();
            ClassName domainTypeName = msgDescriptor.getDomainTypeName(generationContext.configuration().namingManager());
            ClassName builderType = ClassName.get(domainTypeName.canonicalName(), domainTypeName.simpleName() + "Builder");
            typeBuilder.addMethod(MethodSpec.methodBuilder("builder")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addAnnotation(NotNull.class)
                    .returns(builderType)
                    .addParameters(notNullFields.stream().map(Poem::fieldToParameter).toList())
                    .addStatement("return new $T($L)",
                            builderType,
                            Poem.separatedSequence(notNullFields.stream().map(f -> CodeBlock.of("$N", f)).toList(), ",")
                    )
                    .build()
            );
        }
    }

    private void addOneofs(Message msgDescriptor, TypeSpec.Builder typeBuilder) {
        msgDescriptor.getOneofs().forEach(oneOf -> {
            if (oneOf.getFieldsCases().size() < 2) return;
            String oneOfName = Name.toCamelCase(oneOf.getName()) + "Case";
            ClassName domainTypeName = oneOf.getDomainTypeName(generationContext.configuration().namingManager());

            TypeSpec.Builder oneOfSpecBuilder = TypeSpec.enumBuilder(oneOfName).shortEnumNotation(true).addModifiers(Modifier.PUBLIC);
            oneOf.getFieldsCases().forEach(oneOfSpecBuilder::addEnumConstant);
            oneOfSpecBuilder.addEnumConstant("NOT_SET");
            oneOfSpecBuilder.addMethod(MethodSpec.methodBuilder("fromProto")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(ParameterSpec.builder(oneOf.getProtobufTypeName(), "proto").build())
                    .addStatement("""
                                    return switch(proto) {$>
                                    $L
                                    case $L -> NOT_SET;
                                    $<}""",
                            oneOf.getFieldsCases().stream()
                                    .map(c -> CodeBlock.of("case $L -> $L;", c, c))
                                    .collect(Poem.joinCodeBlocks("\n")),
                            oneOf.getName().toUpperCase() + "_NOT_SET"
                    )
                    .returns(domainTypeName)
                    .build()
            );
            TypeSpec oneOfSpec = oneOfSpecBuilder.build();
            typeBuilder.addType(oneOfSpec);

            typeBuilder.addMethod(MethodSpec.methodBuilder("get" + oneOfName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(domainTypeName)
                    .addStatement("return $T.fromProto(toGrpc().get$L())", domainTypeName, oneOfName)
                    .build()
            );
        });
    }

    private void addComponents(List<FieldProcessingResult> processedFields, TypeSpec.Builder typeBuilder) {
        List<FieldSpec> parameters = processedFields.stream()
                .map(FieldProcessingResult::field)
                .toList();
        typeBuilder.addClassComponents(parameters);
    }

    private void implementComparable(@NotNull Message msgDescriptor, TypeSpec.Builder typeBuilder) {
        msgDescriptor.getComparatorReference().ifPresent(comparator -> {
            ClassName type = generatedType(msgDescriptor);
            typeBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(Comparable.class), type));
            ParameterSpec methodParameter = ParameterSpec.builder(type, "rhs").build();
            typeBuilder.addMethod(
                    MethodSpec.methodBuilder("compareTo")
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.INT)
                            .addParameter(methodParameter)
                            .addAnnotation(ClassName.get(Override.class))
                            .addStatement("return $L.compare(this, $N)", comparator, methodParameter)
                            .build()
            );
        });
    }

    private void addTopicField(@NotNull Message msgDescriptor, TypeSpec.Builder typeBuilder) {
        msgDescriptor.getTopic().ifPresent(topic -> typeBuilder.addField(
                FieldSpec.builder(ClassName.get(String.class), "TOPIC", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer(CodeBlock.of("\"$L\"", topic)).build()
        ));
    }

    private void addTransformingMethods(@NotNull Message msgDescriptor, List<FieldProcessingResult> processedFields, TypeSpec.Builder typeBuilder) {
        boolean annotateNotNull = msgDescriptor.getContainingFile().doUseNullabilityAnnotation(false);
        typeBuilder
                .addMethod(new FromGrpcMethodGenerator(
                        generationContext,
                        generatedType(msgDescriptor),
                        protoType(msgDescriptor),
                        processedFields,
                        annotateNotNull
                ).generate())
                .addMethod(new ToGrpcMethodGenerator(
                        generationContext,
                        protoType(msgDescriptor),
                        processedFields,
                        annotateNotNull
                ).generate());
    }

    private List<TypeSpec> generateNested(@NotNull Message msgDescriptor) {
        return msgDescriptor.getNested().stream()
                .filter(EnumOrMessage::doGenerate)
                .map(e -> generationContext.generatorsHolder().generate(e))
                .toList();
    }

    private ClassName protoType(@NotNull Message msgDescriptor) {
        return msgDescriptor.getProtobufTypeName();
    }

    private ClassName generatedType(@NotNull Message msgDescriptor) {
        return msgDescriptor.getDomainTypeName(generationContext.configuration().namingManager());
    }
}
