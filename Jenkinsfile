// ============================================================
//  Pipeline CI/CD — KamerStay
//  Build → Test → Coverage → Docker → Deploy Kubernetes
// ============================================================
pipeline {
    agent any

    environment {
        IMAGE_NAME    = 'kamerstay-backend'
        IMAGE_TAG     = "v${BUILD_NUMBER}"
        K8S_NAMESPACE = 'kamerstay'
        K3D_CLUSTER   = 'kamerstay'
        KUBECONFIG    = '/var/jenkins_home/.kube/config'
    }

    // Vérifie le dépôt toutes les 5 minutes (pas besoin d'IP publique
    // pour un webhook, adapté à un environnement de développement local)
    triggers {
        pollSCM('H/5 * * * *')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Récupération du code source depuis Git"
                checkout scm
                sh 'git log -1 --oneline'
            }
        }

        stage('Build') {
            steps {
                echo "Compilation du module server avec Gradle"
                sh 'chmod +x ./gradlew'
                sh './gradlew :server:build -x test --no-daemon'
            }
        }

        stage('Test') {
            steps {
                echo "Exécution des tests unitaires et d'intégration"
                sh './gradlew :server:test --no-daemon'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                echo "Génération du rapport de couverture"
                sh './gradlew koverHtmlReport --no-daemon || echo "Kover non configuré — étape ignorée"'
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/build/reports/kover/**',
                                     allowEmptyArchive: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Construction de l'image Docker ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
                sh "docker images ${IMAGE_NAME}"
            }
        }

        stage('Push to Cluster Registry') {
            steps {
                echo "Import de l'image dans le cluster k3d"
                sh "k3d image import ${IMAGE_NAME}:${IMAGE_TAG} -c ${K3D_CLUSTER} || echo 'k3d indisponible depuis l agent'"
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo "Déploiement sur le cluster Kubernetes (rolling update)"
                sh """
                    kubectl set image deployment/kamerstay-backend \
                        backend=${IMAGE_NAME}:${IMAGE_TAG} \
                        -n ${K8S_NAMESPACE}
                """
                sh "kubectl rollout status deployment/kamerstay-backend -n ${K8S_NAMESPACE} --timeout=180s"
                sh "kubectl get pods -n ${K8S_NAMESPACE}"
            }
        }

        stage('Smoke Test') {
            steps {
                echo "Vérification que l'application répond après déploiement"
                sh 'curl -sf http://localhost:8080/ || (echo "Application injoignable" && exit 1)'
            }
        }
    }

    post {
        success {
            echo "Pipeline terminé avec succès — version ${IMAGE_TAG} déployée."
        }
        failure {
            echo "Échec du pipeline. Consulter les logs de l'étape en erreur."
        }
        always {
            echo "Build #${BUILD_NUMBER} terminé."
        }
    }
}
