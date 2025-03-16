package model;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Pessoa;
import model.Produto;
import model.Usuario;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-03-13T20:04:02", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Movimento.class)
public class Movimento_ { 

    public static volatile SingularAttribute<Movimento, Integer> idMovimento;
    public static volatile SingularAttribute<Movimento, Pessoa> idPessoa;
    public static volatile SingularAttribute<Movimento, Produto> idProduto;
    public static volatile SingularAttribute<Movimento, Date> dataMovimento;
    public static volatile SingularAttribute<Movimento, Usuario> idUsuario;
    public static volatile SingularAttribute<Movimento, String> tipoMovimento;
    public static volatile SingularAttribute<Movimento, Integer> quantidade;
    public static volatile SingularAttribute<Movimento, BigDecimal> valorUnitario;

}