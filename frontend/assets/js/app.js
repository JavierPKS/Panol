// ==========================================
// CONFIGURACIÓN DE APIS (PUERTOS ASIGNADOS)
// ==========================================
const API_INVENTARIO  = 'http://localhost:8081/api/inventario';
const API_PRESTAMOS   = 'http://localhost:8082/api/prestamos';
const API_SOLICITUDES = 'http://localhost:8083/api/solicitudes';
const API_USUARIOS    = 'http://localhost:8084/api/usuarios';
const API_HISTORIAL   = 'http://localhost:8085/api/historial';
const API_BARCODE     = 'http://localhost:8086/api/barcodes';

// Definición de Roles del Sistema (Debe coincidir con BD: CHAR(1))
const ROLES_SISTEMA = [
    { id: 'A', nombre: 'Administrador' },
    { id: 'P', nombre: 'Pañolero' },
    { id: 'D', nombre: 'Docente' }
    // { id: '4', nombre: 'Alumno' } // Descomentar si agregas Alumno a la tabla ROL en BD
];

// Estado local
let productosPrestamoTemp = [];
let solicitudesCache = [];

// ==========================================
// INICIALIZACIÓN
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    cargarDashboard();
    poblarSelectRoles(); // Cargar opciones correctas en el select de usuarios
});

// Manejo de pestañas
document.querySelectorAll('.navbar li').forEach(item => {
    item.addEventListener('click', () => {
        const vista = item.getAttribute('data-vista');
        document.querySelectorAll('.navbar li').forEach(li => li.classList.remove('active'));
        item.classList.add('active');
        document.querySelectorAll('.vista').forEach(sec => sec.classList.remove('visible'));
        document.getElementById(vista).classList.add('visible');

        if (vista === 'inicio') cargarDashboard();
        if (vista === 'inventario') cargarInventario();
        if (vista === 'solicitudes') cargarSolicitudes();
        if (vista === 'prestamos') cargarPrestamos();
        if (vista === 'historial') cargarHistorial();
        if (vista === 'usuarios') cargarUsuarios();
    });
});

const btnNuevo = document.getElementById('btn-nuevo-producto');
if(btnNuevo) {
    btnNuevo.addEventListener('click', async () => {
        await cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
        abrirModal('modal-crear-producto');
    });
}

// Poblar el select de roles en el modal de usuario para evitar errores de ID
function poblarSelectRoles() {
    const select = document.getElementById('user-rol');
    if (!select) return;
    select.innerHTML = '<option value="">Seleccione Rol...</option>';
    ROLES_SISTEMA.forEach(r => {
        const opt = document.createElement('option');
        opt.value = r.id; // IMPORTANTE: Valor 'A', 'P' o 'D'
        opt.textContent = r.nombre;
        select.appendChild(opt);
    });
}

// ==========================================
// 1. DASHBOARD & KPIS
// ==========================================
async function cargarDashboard() {
    try {
        const res = await fetch(API_INVENTARIO);
        if(!res.ok) throw new Error("Error fetching inventario");
        const productos = await res.json();
        let totalItems = 0;
        productos.forEach(p => totalItems += (p.stock_total || p.stock || 0));
        
        document.getElementById('kpi-total').textContent = totalItems;
        document.getElementById('kpi-disponibles').textContent = totalItems; 
        
        try {
            const resPrest = await fetch(API_PRESTAMOS);
            const dataPrest = await resPrest.json();
            const listaP = dataPrest._embedded ? dataPrest._embedded.solicitudPrestamoDTOList : [];
            const activos = listaP.filter(p => p.estadoSolicitud === 'pendiente' || p.estadoSolicitud === 'aprobado').length;
            document.getElementById('kpi-prestados').textContent = activos;
        } catch(e) {
            document.getElementById('kpi-prestados').textContent = "-";
        }
    } catch (error) { 
        document.getElementById('kpi-total').textContent = "Err";
    }
}

// ==========================================
// 2. INVENTARIO
// ==========================================
async function cargarInventario() {
    const tbody = document.querySelector('#tabla-inventario tbody');
    tbody.innerHTML = '<tr><td colspan="7" class="text-center">Cargando datos...</td></tr>';

    try {
        const res = await fetch(API_INVENTARIO);
        if(!res.ok) throw new Error("Error en API Inventario");
        const data = await res.json();
        tbody.innerHTML = '';
        if(data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center">No hay productos registrados.</td></tr>';
            return;
        }

        data.forEach(item => {
            let ubiTexto = obtenerTextoUbicacion(item.ubicacion);
            const stockMostrar = item.stock_total !== undefined ? item.stock_total : (item.stock || 0);

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><b>${item.codigo || item.cod_interno}</b></td>
                <td>${item.nombre || item.nombre_producto}</td>
                <td>${item.categoria || (item.categoria_prod ? item.categoria_prod.nombre : '-')}</td>
                <td>${item.marca || (item.marca_prod ? item.marca_prod.nombre : '-')}</td>
                <td><small>${ubiTexto}</small></td>
                <td><span style="font-weight:bold; color:${stockMostrar > 0 ? 'green':'red'}">${stockMostrar}</span></td>
                <td>
                    <button class="btn-icon" onclick="verCodigoBarras('${item.codigo || item.cod_interno}')" title="Ver Código"><i class="fa-solid fa-barcode"></i></button>
                    <button class="btn-icon" onclick="prepararEdicion(${item.id || item.id_principal})" title="Editar"><i class="fa-solid fa-pen"></i></button>
                    <button class="btn-icon" style="color:var(--danger)" onclick="eliminarProducto(${item.id || item.id_principal})" title="Eliminar"><i class="fa-solid fa-trash"></i></button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center" style="color:red">Error de conexión (Puerto 8081)</td></tr>';
    }
}

function obtenerTextoUbicacion(ubicacion) {
    if (!ubicacion) return 'Sin asignar';
    if (typeof ubicacion === 'string') return ubicacion;
    if (typeof ubicacion === 'object') {
        const sala = ubicacion.nombreSala || ubicacion.nombre_sala || 'Sala ?';
        const est = ubicacion.estante ? ` - ${ubicacion.estante}` : '';
        return sala + est;
    }
    return 'Desconocida';
}

function verCodigoBarras(codigo) {
    if(!codigo) return alert("Código inválido");
    const urlImagen = `${API_BARCODE}/${codigo}?t=${new Date().getTime()}`; 
    const container = document.getElementById('barcode-container');
    container.innerHTML = `<img src="${urlImagen}" alt="Barcode" style="max-height: 100px;">`;
    document.getElementById('barcode-text').textContent = codigo;
    abrirModal('modal-barcode');
}

function imprimirBarcode() {
    const contenido = document.getElementById('barcode-container').innerHTML;
    const texto = document.getElementById('barcode-text').textContent;
    const win = window.open('', '', 'height=500,width=500');
    win.document.write('<html><head><title>Imprimir</title></head><body style="text-align:center; font-family: sans-serif;">');
    win.document.write('<h3>Duoc UC - Pañol</h3>');
    win.document.write(contenido);
    win.document.write(`<h1>${texto}</h1>`);
    win.document.close();
    win.print();
}

async function guardarProducto() {
    const data = {
        cod_interno: parseInt(document.getElementById('prod-codigo').value),
        nombre_producto: document.getElementById('prod-nombre').value,
        categoria: parseInt(document.getElementById('prod-cat').value),
        marca: parseInt(document.getElementById('prod-marca').value),
        cantidad: parseInt(document.getElementById('prod-stock').value), 
        ubicacion: parseInt(document.getElementById('prod-ubi').value)
    };

    if(!data.cod_interno || !data.nombre_producto) return alert("Complete los campos obligatorios");

    await fetchSimple(API_INVENTARIO, 'POST', data, () => {
        alert("Producto creado correctamente");
        cerrarModal('modal-crear-producto');
        cargarInventario();
        // Limpiar
        document.getElementById('prod-codigo').value = '';
        document.getElementById('prod-nombre').value = '';
        document.getElementById('prod-stock').value = '0';
    });
}

async function prepararEdicion(id) {
    await cargarCombosProducto('edit-cat', 'edit-marca', 'edit-ubi');
    const res = await fetch(`${API_INVENTARIO}/${id}`);
    const data = await res.json();

    document.getElementById('edit-id').value = id;
    document.getElementById('edit-codigo').value = data.cod_interno || data.codigo;
    document.getElementById('edit-nombre').value = data.nombre_producto || data.nombre;
    
    const stockVal = data.stock_total !== undefined ? data.stock_total : data.stock;
    document.getElementById('edit-stock').value = stockVal;
    
    if(data.categoria_id) document.getElementById('edit-cat').value = data.categoria_id;
    if(data.marca_id) document.getElementById('edit-marca').value = data.marca_id;
    if(data.ubicacion_id) document.getElementById('edit-ubi').value = data.ubicacion_id;

    abrirModal('modal-editar-producto');
}

async function guardarEdicion() {
    const id = document.getElementById('edit-id').value;
    const data = {
        nombre_producto: document.getElementById('edit-nombre').value,
        categoria: parseInt(document.getElementById('edit-cat').value),
        marca: parseInt(document.getElementById('edit-marca').value),
        ubicacion: parseInt(document.getElementById('edit-ubi').value),
        stock: parseInt(document.getElementById('edit-stock').value) 
    };

    await fetchSimple(`${API_INVENTARIO}/${id}`, 'PUT', data, () => {
        alert("Actualizado correctamente");
        cerrarModal('modal-editar-producto');
        cargarInventario();
    });
}

async function eliminarProducto(id) {
    if(confirm("¿Seguro que desea eliminar este producto?")) {
        await fetchSimple(`${API_INVENTARIO}/${id}`, 'DELETE', null, cargarInventario);
    }
}

async function cargarCombosProducto(idCat, idMarca, idUbi) {
    try {
        const [resCat, resMarca, resUbi] = await Promise.all([
            fetch(`${API_INVENTARIO}/detalles/categorias`),
            fetch(`${API_INVENTARIO}/detalles/marcas`),
            fetch(`${API_INVENTARIO}/detalles/ubicaciones`)
        ]);
        
        const cats = await resCat.json();
        const marcas = await resMarca.json();
        const ubis = await resUbi.json();

        llenarSelect(idCat, cats, 'id', 'nombre');
        llenarSelect(idMarca, marcas, 'id', 'nombre');
        
        const selectUbi = document.getElementById(idUbi);
        selectUbi.innerHTML = '<option value="">Seleccione...</option>';
        ubis.forEach(u => {
            const opt = document.createElement('option');
            opt.value = u.id;
            const nombre = u.nombreSala || u.nombre_sala || u.nombre || 'Ubicacion';
            const estante = u.estante ? ` (${u.estante})` : '';
            opt.textContent = nombre + estante;
            selectUbi.appendChild(opt);
        });

    } catch (e) { console.error("Error cargando combos", e); }
}

async function guardarCategoria() {
    const nombre = document.getElementById('new-cat-name').value;
    await fetchSimple(`${API_INVENTARIO}/detalles/categorias`, 'POST', {nombre}, () => {
        cerrarModal('modal-add-cat');
        cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
    });
}
async function guardarMarca() {
    const nombre = document.getElementById('new-marca-name').value;
    await fetchSimple(`${API_INVENTARIO}/detalles/marcas`, 'POST', {nombre}, () => {
        cerrarModal('modal-add-marca');
        cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
    });
}
async function guardarUbicacion() {
    const data = {
        nombreSala: document.getElementById('new-ubi-sala').value,
        estante: document.getElementById('new-ubi-estante').value,
        nivel: parseInt(document.getElementById('new-ubi-nivel').value),
        descripcion: document.getElementById('new-ubi-desc').value
    };
    await fetchSimple(`${API_INVENTARIO}/detalles/ubicaciones`, 'POST', data, () => {
        cerrarModal('modal-add-ubi');
        cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
    });
}

// ==========================================
// 3. SOLICITUDES
// ==========================================
async function cargarSolicitudes() {
    const tbody = document.querySelector('#tabla-solicitudes tbody');
    tbody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando...</td></tr>';
    try {
        const res = await fetch(API_SOLICITUDES);
        const data = await res.json();
        solicitudesCache = data._embedded ? data._embedded.solicitudResponseDTOList : (Array.isArray(data) ? data : []);
        
        tbody.innerHTML = '';
        const pendientes = solicitudesCache.filter(s => s.estado === 'pendiente');

        if(pendientes.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay solicitudes pendientes.</td></tr>';
            return;
        }

        pendientes.forEach(s => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${s.id}</td>
                <td>${s.rut}</td>
                <td>${s.motivo}</td>
                <td><span class="badge ${s.prioridad === 'Alta' ? 'badge-danger' : 'badge-info'}">${s.prioridad}</span></td>
                <td>${s.estado}</td>
                <td>
                    <button class="btn-mini btn-primary" onclick="verDetalleSolicitud(${s.id})">
                        <i class="fa-solid fa-eye"></i> Ver Detalle
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) { tbody.innerHTML = '<tr><td colspan="6">Error de conexión</td></tr>'; }
}

async function verDetalleSolicitud(id) {
    const solicitud = solicitudesCache.find(s => s.id === id);
    if (!solicitud) return;

    // 1. Cargar datos básicos de la solicitud (Evitando errores nulos)
    setTextIfExists('det-sol-id', solicitud.id);
    setTextIfExists('det-user-rut', solicitud.rut); // Corregido: ID correcto del HTML
    setTextIfExists('det-sol-prio', solicitud.prioridad);
    setTextIfExists('det-sol-motivo', solicitud.motivo);

    // 2. Resetear campos de usuario mientras cargan
    setTextIfExists('det-user-nombre', 'Cargando...');
    setTextIfExists('det-user-email', '...');
    setTextIfExists('det-user-rol', '...');

    // 3. Activar botones (Ahora sí funcionarán porque el código no se rompe antes)
    const btnAprobar = document.getElementById('btn-aprobar-modal');
    const btnRechazar = document.getElementById('btn-rechazar-modal');

    if (btnAprobar) {
        btnAprobar.onclick = () => cambiarEstadoSol(id, 'aprobado'); 
        // Deshabilitar si no está pendiente
        btnAprobar.disabled = (solicitud.estado !== 'pendiente');
    }
    
    if (btnRechazar) {
        btnRechazar.onclick = () => cambiarEstadoSol(id, 'rechazado');
        btnRechazar.disabled = (solicitud.estado !== 'pendiente');
    }

    // 4. Abrir Modal y preparar tabla
    const tbody = document.querySelector('#tabla-detalle-items tbody');
    if (tbody) tbody.innerHTML = '';
    const loading = document.getElementById('detalle-loading');
    if (loading) loading.classList.remove('hidden');

    abrirModal('modal-detalle-solicitud');

    // 5. OBTENER DATOS DEL USUARIO (Nueva funcionalidad)
    // Esto busca el nombre y rol usando el RUT que viene en la solicitud
    if (solicitud.rut) {
        try {
            const resUser = await fetch(`${API_USUARIOS}/${solicitud.rut}`);
            if (resUser.ok) {
                const user = await resUser.json();
                
                // Formatear nombre
                const nombreCompleto = `${user.pnombre} ${user.apPaterno}`;
                setTextIfExists('det-user-nombre', nombreCompleto);
                setTextIfExists('det-user-email', user.email);

                // Mapear el Rol (A -> Administrador, etc.)
                const rolObj = ROLES_SISTEMA.find(r => r.id === user.idRol);
                const nombreRol = rolObj ? rolObj.nombre : (user.nombreRol || user.idRol);
                setTextIfExists('det-user-rol', nombreRol);
            } else {
                setTextIfExists('det-user-nombre', 'Usuario no encontrado en BD');
            }
        } catch (e) {
            console.error("Error cargando usuario:", e);
            setTextIfExists('det-user-nombre', 'Error de conexión (API Usuarios)');
        }
    }

    // 6. Cargar detalles de los productos (Lógica original mejorada)
    try {
        const promesas = solicitud.detalles.map(async (detalle) => {
            try {
                const respProd = await fetch(`${API_INVENTARIO}/${detalle.idProducto}`);
                if (!respProd.ok) throw new Error();
                const prodData = await respProd.json();
                return { ...detalle, prodInfo: prodData };
            } catch (e) {
                return { ...detalle, prodInfo: null };
            }
        });

        const detallesCompletos = await Promise.all(promesas);

        detallesCompletos.forEach(d => {
            const prod = d.prodInfo;
            const nombreProd = prod ? (prod.nombre_producto || prod.nombre) : `ID ${d.idProducto}`;
            const stockActual = prod ? (prod.stock_total !== undefined ? prod.stock_total : prod.stock) : '?';
            const ubicacion = prod ? obtenerTextoUbicacion(prod.ubicacion) : '-';
            
            // Lógica visual para stock
            const alertaStock = (typeof stockActual === 'number' && stockActual < d.cantidad) 
                                ? '<span style="color:red; font-weight:bold"><i class="fa-solid fa-triangle-exclamation"></i> Insuficiente</span>' 
                                : '<span style="color:green">OK</span>';

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><b>${nombreProd}</b></td>
                <td>${ubicacion}</td>
                <td class="text-center font-bold">${d.cantidad}</td>
                <td class="text-center">${stockActual} (${alertaStock})</td>
                <td><small>${d.fechaInicio || '-'} / ${d.fechaRetorno || '-'}</small></td>
            `;
            if (tbody) tbody.appendChild(tr);
        });

    } catch (error) {
        if (tbody) tbody.innerHTML = '<tr><td colspan="5">Error cargando detalles.</td></tr>';
    } finally {
        if (loading) loading.classList.add('hidden');
    }
}

// Función auxiliar para evitar errores si el HTML cambia
function setTextIfExists(elementId, text) {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = text;
    }
}

async function cambiarEstadoSol(id, estado) {
    if(!confirm(`¿Confirma marcar la solicitud como ${estado}?`)) return;
    
    // Llamada a la API para actualizar el estado
    await fetchSimple(`${API_SOLICITUDES}/${id}/estado`, 'PUT', {estado}, () => {
        cerrarModal('modal-detalle-solicitud'); // Cierra el modal
        cargarSolicitudes(); // Recarga la lista de fondo
        alert(`Solicitud ${estado} correctamente.`);
    });
}

// ==========================================
// 4. PRÉSTAMOS
// ==========================================
async function cargarPrestamos() {
    const tbody = document.querySelector('#tabla-prestamos tbody');
    tbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando...</td></tr>';
    
    try {
        const res = await fetch(API_PRESTAMOS);
        if (!res.ok) throw new Error("Error fetching préstamos");
        const data = await res.json();
        
        // Obtener la lista cruda
        const listaCompleta = data._embedded ? data._embedded.solicitudPrestamoDTOList : (Array.isArray(data) ? data : []);

        // FILTRO CLAVE: Solo mostrar los que NO están devueltos
        const listaActivos = listaCompleta.filter(p => p.estadoSolicitud !== 'Devuelto');

        tbody.innerHTML = '';
        
        if(listaActivos.length === 0) {
             tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay préstamos activos.</td></tr>';
             return;
        }

        // Ordenar: Pendientes más recientes primero
        listaActivos.sort((a, b) => b.idPrestamo - a.idPrestamo);

        listaActivos.forEach(p => {
            const tr = document.createElement('tr');
            
            // Determinar color del badge
            let badgeClass = 'badge-info';
            if(p.estadoSolicitud === 'pendiente') badgeClass = 'badge-warning';
            if(p.estadoSolicitud === 'Atrasado') badgeClass = 'badge-danger';

            tr.innerHTML = `
                <td><b>${p.idPrestamo}</b></td>
                <td>${p.usuarioRut}</td>
                <td>${p.fechaSolicitud}</td>
                <td><span class="badge ${badgeClass}">${p.estadoSolicitud}</span></td>
                <td>
                    <button class="btn-mini btn-primary" onclick="devolverPrestamo(${p.idPrestamo})">
                        <i class="fa-solid fa-rotate-left"></i> Devolver
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) { 
        console.error(e);
        tbody.innerHTML = '<tr><td colspan="5" style="color:red">Error cargando préstamos (Puerto 8082)</td></tr>'; 
    }
}

async function abrirModalPrestamo() {
    productosPrestamoTemp = [];
    actualizarListaProductosModal();
    const res = await fetch(API_INVENTARIO);
    const prods = await res.json();
    llenarSelect('prestamo-prod-select', prods, 'id', 'nombre_producto'); 
    abrirModal('modal-nuevo-prestamo');
}

function agregarProductoALista() {
    const select = document.getElementById('prestamo-prod-select');
    const inputCant = document.getElementById('prestamo-prod-cant');
    
    const id = select.value;
    const nombre = select.options[select.selectedIndex]?.text;
    const cant = parseInt(inputCant.value);

    // 1. Validaciones
    if (!id || id === "") {
        alert("⚠️ Debes seleccionar un producto válido.");
        return;
    }
    if (isNaN(cant) || cant <= 0) {
        alert("⚠️ La cantidad debe ser mayor a 0.");
        return;
    }

    // 2. Agregar a la lista temporal
    productosPrestamoTemp.push({ 
        productoIdPrincipal: parseInt(id), 
        cantidad: cant, 
        nombreDisplay: nombre 
    });

    // 3. Limpiar campo de cantidad y refrescar vista
    inputCant.value = "1"; 
    actualizarListaProductosModal();
}

function actualizarListaProductosModal() {
    const div = document.getElementById('lista-productos-prestamo');
    div.innerHTML = '';
    if(productosPrestamoTemp.length === 0) {
        div.innerHTML = '<p class="text-muted text-center">Sin productos</p>';
        return;
    }
    productosPrestamoTemp.forEach((p, idx) => {
        const item = document.createElement('div');
        item.className = 'product-item';
        item.innerHTML = `<span>${p.nombreDisplay} (x${p.cantidad})</span> 
                          <button class="btn-icon" onclick="productosPrestamoTemp.splice(${idx},1); actualizarListaProductosModal();" style="color:red"><i class="fa-solid fa-times"></i></button>`;
        div.appendChild(item);
    });
}

async function confirmarPrestamo() {
    const rutInput = document.getElementById('prestamo-rut');
    const motivoInput = document.getElementById('prestamo-motivo');
    const prioridadInput = document.getElementById('prestamo-prioridad');

    const rut = parseInt(rutInput.value);
    const motivo = motivoInput.value.trim();
    const prioridad = prioridadInput.value;

    // --- VALIDACIONES PREVIAS ---
    
    // 1. Validar RUT
    if (!rut || isNaN(rut)) {
        alert("⚠️ Por favor ingresa un RUT de usuario válido (sin dígito verificador).");
        rutInput.focus();
        return;
    }

    // 2. Validar Motivo
    if (motivo.length < 3) {
        alert("⚠️ Debes ingresar un motivo para el préstamo.");
        motivoInput.focus();
        return;
    }

    // 3. Validar que hay productos agregados
    if (productosPrestamoTemp.length === 0) {
        alert("⚠️ La lista de productos está vacía. Agrega al menos un ítem.");
        return;
    }

    // --- PREPARAR ENVÍO ---
    const body = {
        usuarioRut: rut,
        motivoPrestamo: motivo,
        prioridad: prioridad,
        detalles: productosPrestamoTemp.map(p => ({
            productoIdPrincipal: p.productoIdPrincipal,
            cantidad: p.cantidad
        }))
    };

    // --- ENVIAR AL SERVIDOR ---
    // Usamos fetchSimple para manejar errores automáticamente
    await fetchSimple(API_PRESTAMOS, 'POST', body, () => {
        alert("✅ ¡Préstamo registrado exitosamente!");
        cerrarModal('modal-nuevo-prestamo');
        
        // Limpiar formulario completo
        rutInput.value = '';
        motivoInput.value = '';
        productosPrestamoTemp = [];
        actualizarListaProductosModal();
        
        // Recargar tabla
        cargarPrestamos();
    });
}

async function devolverPrestamo(id) {
    if(!confirm("¿Confirmar la recepción de los recursos? El estado cambiará a DEVUELTO.")) return;

    // Llamada directa al backend
    await fetchSimple(`${API_PRESTAMOS}/${id}/devolver`, 'PUT', null, () => {
        // Feedback visual inmediato
        alert("Devolución registrada correctamente.");
        
        // Recargar la tabla para que el filtro oculte el préstamo devuelto
        cargarPrestamos(); 
        
        // Actualizar los contadores del dashboard también
        cargarDashboard();
    });
}

// ==========================================
// 5. USUARIOS & HISTORIAL
// ==========================================
async function cargarUsuarios() {
    const tbody = document.querySelector('#tabla-usuarios tbody');
    tbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando...</td></tr>';
    
    try {
        const res = await fetch(API_USUARIOS);
        const data = await res.json();
        const lista = data._embedded ? data._embedded.usuarioDTOList : [];
        
        tbody.innerHTML = '';
        lista.forEach(u => {
            // Mapeo seguro usando el ID del rol ('A' -> 'Administrador')
            const rolObj = ROLES_SISTEMA.find(r => r.id === u.idRol);
            const rolNombre = rolObj ? rolObj.nombre : (u.nombreRol || u.idRol);

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${u.rut}-${u.dvRut}</td>
                <td>${u.pnombre} ${u.apPaterno}</td>
                <td>${u.email}</td>
                <td>${rolNombre}</td>
                <td><button class="btn-icon" onclick="eliminarUsuario(${u.rut})" title="Borrar"><i class="fa-solid fa-trash"></i></button></td>
            `;
            tbody.appendChild(tr);
        });
    } catch(e) { console.error(e); tbody.innerHTML = '<tr><td colspan="5">Error usuarios</td></tr>'; }
}

async function guardarUsuario() {
    // FIX: Obtener el valor DIRECTO (String) del select, NO usar parseInt
    const rolSeleccionado = document.getElementById('user-rol').value;

    if(!rolSeleccionado) {
        alert("Seleccione un rol");
        return;
    }

    const data = {
        rut: parseInt(document.getElementById('user-rut').value),
        dvRut: document.getElementById('user-dv').value,
        pnombre: document.getElementById('user-pnombre').value,
        snombre: document.getElementById('user-snombre') ? document.getElementById('user-snombre').value : "",
        apPaterno: document.getElementById('user-apPaterno').value,
        apMaterno: document.getElementById('user-apMaterno') ? document.getElementById('user-apMaterno').value : "",
        email: document.getElementById('user-email').value,
        idRol: rolSeleccionado // Envia 'A', 'P' o 'D'
    };
    
    await fetchSimple(API_USUARIOS, 'POST', data, () => {
        alert("Usuario creado correctamente");
        cerrarModal('modal-crear-usuario');
        cargarUsuarios();
        // Limpiar formulario
        document.getElementById('user-rut').value = '';
        document.getElementById('user-email').value = '';
    });
}

async function eliminarUsuario(rut) {
    if(confirm("¿Eliminar usuario?")) {
        await fetchSimple(`${API_USUARIOS}/${rut}`, 'DELETE', null, cargarUsuarios);
    }
}

async function cargarHistorial() {
    const tbody = document.querySelector('#tabla-historial tbody');
    tbody.innerHTML = `
        <tr>
            <td colspan="6" class="text-center">Cargando historial...</td>
        </tr>
    `;

    try {
        const res = await fetch(API_HISTORIAL);

        if (!res.ok) {
            throw new Error('Error en API Historial');
        }

        const data = await res.json();
        const lista = data._embedded ? data._embedded.historialRowList : [];

        tbody.innerHTML = '';

        if (lista.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center">
                        No existen movimientos registrados
                    </td>
                </tr>
            `;
            return;
        }

        lista.forEach(h => {
            const tr = document.createElement('tr');

            tr.innerHTML = `
                <td>${h.idDetalle}</td>
                <td>${h.nombreProducto || '-'}</td>
                <td>${h.cantidad}</td>
                <td>
                    <span style="
                        font-weight: bold;
                        color: ${
                            h.estado === 'DEVUELTO'
                                ? 'green'
                                : h.estado === 'ATRASADO'
                                ? 'red'
                                : 'orange'
                        };
                    ">
                        ${h.estado}
                    </span>
                </td>
                <td>${h.observacion}</td>
                <td>${h.fechaRegistro}</td>
            `;

            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error(error);
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center" style="color:red">
                    Error al cargar historial (API Historial)
                </td>
            </tr>
        `;
    }
}


// ==========================================
// UTILIDADES
// ==========================================
function abrirModal(id) { document.getElementById(id).classList.remove('hidden'); }
function cerrarModal(id) { document.getElementById(id).classList.add('hidden'); }
function abrirModalUsuario() { 
    poblarSelectRoles(); // Asegurar que el combo tenga los roles correctos
    abrirModal('modal-crear-usuario'); 
}

function llenarSelect(id, data, valField, labelField) {
    const sel = document.getElementById(id);
    if(!sel) return;
    sel.innerHTML = '<option value="">Seleccione...</option>';
    data.forEach(item => {
        const opt = document.createElement('option');
        opt.value = item[valField];
        opt.textContent = item[labelField] || item.nombre; 
        sel.appendChild(opt);
    });
}

async function fetchSimple(url, method, body, onSuccess) {
    try {
        const opts = { method: method, headers: {'Content-Type': 'application/json'} };
        if(body) opts.body = JSON.stringify(body);
        
        const res = await fetch(url, opts);
        if(res.ok) {
            onSuccess();
        } else {
            const txt = await res.text();
            // Intenta parsear el error JSON del backend si existe
            try {
                const jsonError = JSON.parse(txt);
                alert("Error: " + (jsonError.message || jsonError.error));
            } catch(e) {
                alert("Error del servidor: " + txt);
            }
        }
    } catch(e) {
        alert("Error de conexión: " + e.message);
    }
}