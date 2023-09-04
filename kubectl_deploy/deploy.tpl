apiVersion: apps/v1
kind: Deployment
metadata:
  name: caab-api
  namespace: laa-ccms-civil
  labels:
    app.kubernetes.io/name: caab-api
spec:
  replicas: 1
  selector:
    matchLabels:
      app.kubernetes.io/name: caab-api
  template:
    metadata:
      labels:
        app.kubernetes.io/name: caab-api
    spec:
      containers:
        - name: caab-api
          image: ${ECR_URL}:${IMAGE_TAG}
          ports:
            - containerPort: 8005
          env:
            - name: CAAB_DATASOURCE_URL
              valueFrom:
                secretKeyRef:
                  name: saml-metadata-uri
                  key: caab-datasource-url
            - name: CAAB_DATASOURCE_USERNAME
              valueFrom:
                secretKeyRef:
                  name: saml-metadata-uri
                  key: caab-datasource-username
            - name: CAAB_DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: saml-metadata-uri
                  key: caab-datasource-password
