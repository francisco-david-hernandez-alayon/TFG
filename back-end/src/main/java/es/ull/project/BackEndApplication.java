package es.ull.project;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Condition;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import es.ull.project.adapter.mongodb.repository.WorkFlowMongoRepository;
import es.ull.project.adapter.mongodb.repository.OperationMongoRepository;
import es.ull.project.application.service.operation.OperationCreateService;
import es.ull.project.application.service.workflow.WorkFlowCreateService;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.enumerate.NodeColor;
import es.ull.project.domain.enumerate.NodeIcon;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.XPosition;
import es.ull.project.domain.valueobject.YPosition;
import es.ull.project.domain.valueobject.Conditional;
import es.ull.project.domain.valueobject.ConditionalName;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.FileName;
import es.ull.project.domain.valueobject.Link;
import es.ull.project.domain.valueobject.LinkName;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeName;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

@SpringBootApplication
public class BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
		// System.out.println(UllString.randomAlphanumeric(3));
		// System.out.println(UllRandom.random(5, 250));
	}

	@Bean
	CommandLineRunner run(
			WorkFlowCreateService flowCreateService,
			OperationCreateService operationCreateService,
			OperationMongoRepository operationRepository,
			WorkFlowMongoRepository flowRepository) {
		return args -> {
			System.out.println("=== BACKEND ===");
			System.out.println("=== BACKEND ===");
			System.out.println("=== BACKEND ===");
			System.out.println("=== Iniciando pruebas de escritura en MongoDB ===");
			try {
				Random random = new Random();
				// Random name generators
				String randomFileName = "file" + String.format("%06d", random.nextInt(1_000_000)) + ".txt";
				String randomConditionalName = "conditional" + String.format("%06d", random.nextInt(1_000_000));
				String randomOp1Name = "operation" + String.format("%06d", random.nextInt(1_000_000));
				String randomOp2Name = "operation" + String.format("%06d", random.nextInt(1_000_000));
				String randomNode1Name = "Initialnode" + String.format("%06d", random.nextInt(1_000_000));
				String randomNode2Name = "nodeConditional" + String.format("%06d", random.nextInt(1_000_000));
				String randomNode3Name = "node" + String.format("%06d", random.nextInt(1_000_000));
				String randomNode4Name = "node" + String.format("%06d", random.nextInt(1_000_000));
				String randomFlowName = "workflow" + String.format("%06d", random.nextInt(1_000_000));
				File file1 = new File(
						new FileName(randomFileName), new URI("https://example-files.online-convert.com/document/txt/example.txt"));
				Conditional conditional1 = new Conditional(new ConditionalName(randomConditionalName),
						"result = len(file) < 100",
						Map.of(
								new NodeName(randomNode3Name), true,
								new NodeName(randomNode4Name), false));
				Operation op1 = operationCreateService.createOperation(
						new OperationName(randomOp1Name),
						new UllDockerImageName("fdavidhernandez/uppercase:latest"));
				Operation op2 = operationCreateService.createOperation(
						new OperationName(randomOp2Name),
						new UllDockerImageName("fdavidhernandez/head10:latest"));
				System.out.println("=== Operations guardadas correctamente en MongoDB ===");
				// Create nodes
				Node node1 = new Node(
						new NodeName(randomNode1Name),
						new XPosition(BigDecimal.valueOf(0)),
						new YPosition(new BigDecimal(0)),
						NodeColor.GRAY,
						NodeIcon.START,
						file1, new ArrayList<>());
				Node node2 = new Node(
						new NodeName(randomNode2Name),
						new XPosition(new BigDecimal(0)),
						new YPosition(new BigDecimal(200.234)),
						NodeColor.RED,
						NodeIcon.GEAR,
						conditional1, 
						new ArrayList<>());
				Node node3 = new Node(
						new NodeName(randomNode3Name),
						new XPosition(new BigDecimal(150)),
						new YPosition(new BigDecimal(400.32432432)),
						NodeColor.BLUE,
						NodeIcon.ROBOT,
						op1, 
						new ArrayList<>());
				Node node4 = new Node(
						new NodeName(randomNode4Name),
						new XPosition(new BigDecimal(-150)),
						new YPosition(new BigDecimal(400.32432432)),
						NodeColor.BLUE,
						NodeIcon.ROBOT,
						op2, 
						new ArrayList<>());
				// Create links
				Link link1 = new Link(new LinkName("linkToConditional"), node1.getName(), node2.getName(), true);
				Link link2 = new Link(new LinkName("linkTrue"), node2.getName(), node3.getName(), true);
				Link link3 = new Link(new LinkName("linkFalse"), node2.getName(), node4.getName(), true);
				node1 = node1.addLink(link1);
				node2 = node2.addLink(link2);
				node2 = node2.addLink(link3);
				List<Node> nodes = new ArrayList<>();
				nodes.add(node1);
				nodes.add(node2);
				nodes.add(node3);
				nodes.add(node4);
				// CREATE workflow
				WorkFlowName flowName = new WorkFlowName(randomFlowName);
				WorkFlow createdWorkFlow = flowCreateService.createWorkFlow(flowName, nodes, true);
				System.out.println("-----> WORKFLOW.TOSTRING(): " + createdWorkFlow.toString());
				System.out.println("=== Workflow guardado correctamente en MongoDB ===");
			} catch (Exception e) {
				System.out.println("Error al crear el flujo:");
				e.printStackTrace();
				return;
			}
		};
	}
}